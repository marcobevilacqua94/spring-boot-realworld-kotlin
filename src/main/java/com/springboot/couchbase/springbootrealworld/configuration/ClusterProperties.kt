package com.springboot.couchbase.springbootrealworld.configuration

import lombok.Getter
import lombok.Setter
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "couchbase")
data class ClusterProperties(
        var connectionString: String? = null,
        var username: String? = null,
        var password: String? = null,
        var defaultBucket: String? = null,
        var defaultScope: String? = null,
        var defaultCollection: String? = null,
        var useCapella: Boolean = false
)