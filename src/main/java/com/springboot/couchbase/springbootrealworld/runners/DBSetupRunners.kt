package com.springboot.couchbase.springbootrealworld.runners

import com.couchbase.client.core.error.CollectionExistsException
import com.couchbase.client.core.error.IndexNotFoundException
import com.couchbase.client.core.error.InternalServerFailureException
import com.couchbase.client.core.retry.reactor.Retry
import com.couchbase.client.java.Bucket
import com.couchbase.client.java.Cluster
import com.couchbase.client.java.manager.collection.CollectionManager
import com.couchbase.client.java.manager.collection.CollectionSpec
import com.couchbase.client.java.manager.query.CreatePrimaryQueryIndexOptions
import com.couchbase.client.java.manager.query.WatchQueryIndexesOptions
import com.couchbase.client.java.query.QueryResult
import com.springboot.couchbase.springbootrealworld.configuration.ClusterProperties
import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument
import com.springboot.couchbase.springbootrealworld.domain.article.entity.CommentDocument
import com.springboot.couchbase.springbootrealworld.domain.article.entity.FavoriteDocument
import com.springboot.couchbase.springbootrealworld.domain.profile.entity.FollowDocument
import com.springboot.couchbase.springbootrealworld.domain.tag.entity.ArticleTagRelationDocument
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class DBSetupRunners : CommandLineRunner {

    private val logger: Logger = LoggerFactory.getLogger(DBSetupRunners::class.java)

    @Autowired
    private lateinit var couchbaseTemplate: CouchbaseTemplate

    @Autowired
    private lateinit var clusterProperties: ClusterProperties

    companion object {
        private val WATCH_PRIMARY = WatchQueryIndexesOptions.watchQueryIndexesOptions().watchPrimary(true)
        private const val DEFAULT_INDEX_NAME = "#primary"
    }

    override fun run(vararg args: String?) {
        val cluster: Cluster = couchbaseTemplate.couchbaseClientFactory.cluster
        val bucket: Bucket = couchbaseTemplate.couchbaseClientFactory.bucket

        val defaultBucket: String = clusterProperties.defaultBucket
        val defaultScope: String = clusterProperties.defaultScope

        try {
            cluster.queryIndexes().createPrimaryIndex(defaultBucket)
            logger.info("Created primary index $defaultBucket")
        } catch (e: Exception) {
            logger.info("Primary index already exists on bucket $defaultBucket")
        }

        val collections = listOf(
                UserDocument.USER_COLLECTION_NAME,
                ArticleDocument.ARTICLE_COLLECTION_NAME,
                FavoriteDocument.FAVORITE_COLLECTION_NAME,
                CommentDocument.COMMENT_COLLECTION_NAME,
                ArticleTagRelationDocument.TAG_COLLECTION_NAME,
                FollowDocument.FOLLOW_COLLECTION_NANE
        )

        collections.forEach { col -> createCollection(bucket, defaultScope, col) }
        collections.forEach { col -> setupPrimaryIndex(cluster, defaultBucket, defaultScope, col) }

        try {
            val result: QueryResult = cluster.query("CREATE INDEX default_profile_title_index ON ${bucket.name()}._default.${ArticleDocument.ARTICLE_COLLECTION_NAME}(title)")
            Thread.sleep(1000)
        } catch (e: Exception) {
            logger.info("Failed to create secondary index on article.title: ${e.message}")
        }

        logger.info("Application is ready.")
    }

    private fun setupPrimaryIndex(cluster: Cluster, bucketName: String, scope: String, collectionName: String) {
        Mono.fromRunnable { createIndex(cluster, bucketName, scope, collectionName) }
                .retryWhen(
                        Retry.onlyIf { ctx ->
                            findCause(ctx.exception(), InternalServerFailureException::class.java)
                                    .filter { exception ->
                                        CouchbaseError.create(exception)
                                                .errorEntries.any { err -> err.message.contains("GSI") }
                                    }
                                    .isPresent
                        }
                                .exponentialBackoff(Duration.ofMillis(50), Duration.ofSeconds(3))
                                .timeout(Duration.ofSeconds(60))
                                .toReactorRetry()
                )
                .block()

        Mono.fromRunnable { waitForIndex(cluster, bucketName, scope, collectionName) }
                .retryWhen(
                        Retry.onlyIf { ctx -> hasCause(ctx.exception(), IndexNotFoundException::class.java) }
                                .exponentialBackoff(Duration.ofMillis(50), Duration.ofSeconds(3))
                                .timeout(Duration.ofSeconds(30))
                                .toReactorRetry()
                )
                .block()

        IndexCommons.waitUntilReady(cluster, bucketName, Duration.ofSeconds(60))
    }

    private fun createCollection(bucket: Bucket, scope: String, collectionName: String) {
        val collectionManager: CollectionManager = bucket.collections()
        try {
            val spec: CollectionSpec = CollectionSpec.create(collectionName, scope)
            collectionManager.createCollection(spec)
            logger.info("Created collection '${spec.name()}' in scope '${spec.scopeName()}' of bucket '${bucket.name()}'")
        } catch (e: CollectionExistsException) {
            logger.info("Collection <$collectionName> already exists")
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun createIndex(cluster: Cluster, bucketName: String, scope: String, collection: String?) {
        val options: CreatePrimaryQueryIndexOptions = CreatePrimaryQueryIndexOptions.createPrimaryQueryIndexOptions()
                .ignoreIfExists(true)
                .numReplicas(0)
        if (collection != null && scope != null) {
            options.collectionName(collection).scopeName(scope)
        }
        cluster.queryIndexes().createPrimaryIndex(bucketName, options)
    }

    private fun waitForIndex(cluster: Cluster, bucketName: String, scope: String, collection: String?) {
        if (collection != null && scope != null) {
            WATCH_PRIMARY.collectionName(collection).scopeName(scope)
        }
        cluster.queryIndexes().watchIndexes(bucketName, listOf(DEFAULT_INDEX_NAME), Duration.ofSeconds(10), WATCH_PRIMARY)
    }
}
