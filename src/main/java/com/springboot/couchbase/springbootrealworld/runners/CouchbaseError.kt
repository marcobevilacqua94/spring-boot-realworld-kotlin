package com.springboot.couchbase.springbootrealworld.runners

import com.couchbase.client.core.error.CouchbaseException

data class CouchbaseError(val errorEntries: List<ErrorEntry>) {
    companion object {
        private const val UNKNOWN = "UNKNOWN"

        fun create(exception: CouchbaseException): CouchbaseError {
            val context = exception.context()
            if (context == null) {
                return unknown(exception)
            }

            val exported = mutableMapOf<String, Any>()
            context.injectExportableParams(exported)

            val errors = exported["errors"]
            if (errors !is List<*>) {
                return unknown(exception)
            }

            val entries = errors.mapNotNull { errorObject ->
                if (errorObject is Map<*, *>) {
                    val errorCode = errorObject["code"]?.toString()
                    val message = errorObject["message"]?.toString()
                    if (errorCode != null && message != null) {
                        ErrorEntry(errorCode, message)
                    } else {
                        null
                    }
                } else {
                    null
                }
            }

            return CouchbaseError(entries)
        }

        private fun unknown(exception: CouchbaseException): CouchbaseError {
            return CouchbaseError(listOf(ErrorEntry(UNKNOWN, exception.toString())))
        }
    }
}

data class ErrorEntry(val errorCode: String, val message: String)
