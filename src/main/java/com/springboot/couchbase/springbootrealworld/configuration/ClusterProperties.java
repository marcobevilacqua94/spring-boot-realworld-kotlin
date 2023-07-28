package com.springboot.couchbase.springbootrealworld.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "couchbase")
@Getter
@Setter
public class ClusterProperties {

    private String connectionString, username, password, defaultBucket, defaultScope, defaultCollection;

    private boolean useCapella;

}