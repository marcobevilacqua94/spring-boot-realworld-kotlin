package com.springboot.couchbase.springbootrealworld.runners

class IndexInfo(val name: String, val bucket: String, val status: String, val lastScanTime: String) {

    companion object {
        fun create(asMap: Map<*, *>): IndexInfo {
            val name = asMap["index"] as String
            val bucket = asMap["bucket"] as String
            val status = asMap["status"] as String
            val lastScanTime = asMap["lastScanTime"] as String
            return IndexInfo(name, bucket, status, lastScanTime)
        }
    }

    fun getQualified(): String {
        return "$bucket:$name"
    }

    override fun toString(): String {
        return "IndexInfo{" +
                "name='$name'" +
                ", bucket='$bucket'" +
                ", status='$status'" +
                ", lastScanTime='$lastScanTime'" +
                '}'
    }
}
