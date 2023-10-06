package com.springboot.couchbase.springbootrealworld.runners

import com.couchbase.client.core.error.IndexesNotReadyException
import com.couchbase.client.core.retry.reactor.Retry
import com.couchbase.client.core.retry.reactor.RetryExhaustedException
import com.couchbase.client.java.Cluster
import com.couchbase.client.java.http.CouchbaseHttpClient
import com.couchbase.client.java.http.HttpPath
import com.couchbase.client.java.http.HttpResponse
import com.couchbase.client.java.http.HttpTarget
import com.couchbase.client.java.json.JsonObject
import reactor.core.publisher.Mono
import java.sql.SQLException
import java.time.Duration
import java.util.concurrent.TimeoutException

import com.couchbase.client.core.logging.RedactableArgument.redactMeta
import com.couchbase.client.core.util.CbThrowables.findCause
import com.couchbase.client.core.util.CbThrowables.hasCause
import java.util.stream.Collectors.toList
import java.util.Collections.singletonMap
import java.util.stream.Collectors.toMap

object IndexCommons {

    fun waitUntilReady(cluster: Cluster, bucketName: String, timeout: Duration) {
        waitInner(timeout) { failIfIndexesOffline(cluster.httpClient(), bucketName) }
    }

    fun waitUntilOffline(cluster: Cluster, bucketName: String, timeout: Duration) {
        waitInner(timeout) { failIfIndexesPresent(cluster.httpClient(), bucketName) }
    }

    private fun waitInner(timeout: Duration, runnable: () -> Unit) {
        Mono.fromRunnable(runnable)
                .retryWhen(Retry.onlyIf { ctx -> hasCause(ctx.exception(), IndexesNotReadyException::class.java) }
                        .exponentialBackoff(Duration.ofMillis(50), Duration.ofSeconds(3))
                        .timeout(timeout)
                        .toReactorRetry())
                .onErrorMap { t ->
                    if (t is RetryExhaustedException) toWatchTimeoutException(t, timeout) else t
                }
                .block()
    }

    private fun toWatchTimeoutException(t: Throwable, timeout: Duration): TimeoutException {
        val msg = "A requested index is still not ready after $timeout."
        findCause(t, IndexesNotReadyException::class.java).ifPresent { cause ->
            msg.append(" Unready index name -> state: ${redactMeta(cause.indexNameToState())}")
        }
        return TimeoutException(msg.toString())
    }

    private fun failIfIndexesPresent(httpClient: CouchbaseHttpClient, bucketName: String) {
        val matchingIndexes = getMatchingIndexInfo(httpClient, bucketName)
                .stream()
                .collect(toMap(IndexInfo::qualified, IndexInfo::status))

        if (matchingIndexes.isNotEmpty()) {
            throw IndexesNotReadyException(matchingIndexes)
        }
    }

    @Throws(IndexesNotReadyException::class)
    private fun failIfIndexesOffline(httpClient: CouchbaseHttpClient, bucketName: String) {
        val matchingIndexes = getMatchingIndexInfo(httpClient, bucketName)
        if (matchingIndexes.isEmpty()) {
            throw IndexesNotReadyException(singletonMap("#primary", "notFound"))
        }

        val offlineIndexNameToState = matchingIndexes
                .filter { idx -> idx.status != "Ready" }
                .associateBy({ it.qualified }, { it.status })

        if (offlineIndexNameToState.isNotEmpty()) {
            throw IndexesNotReadyException(offlineIndexNameToState)
        }
    }

    fun getIndexes(httpClient: CouchbaseHttpClient): List<Any> {
        try {
            val response = httpClient.get(HttpTarget.manager(), HttpPath.of("/indexStatus"))
            if (!response.success()) {
                throw SQLException("Failed to retrieve index information: Response status=${response.statusCode()} Response body=${response.contentAsString()}")
            }
            return JsonObject.fromJson(response.content()).getArray("indexes").toList()
        } catch (ex: SQLException) {
            throw ex
        } catch (ex: Exception) {
            throw SQLException(ex)
        }
    }

    private fun getMatchingIndexInfo(httpClient: CouchbaseHttpClient, bucketName: String): List<IndexInfo> {
        val list: List<Any>
        try {
            list = getIndexes(httpClient)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }

        return list.stream()
                .filter { i -> i is Map<*, *> }
                .map { i -> IndexInfo.create(i as Map<*, *>) }
                .filter { i -> bucketName == i.bucket && "#primary" == i.name }
                .collect(toList())
    }
}
