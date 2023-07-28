package com.springboot.couchbase.springbootrealworld.configuration;

import com.couchbase.client.java.env.ClusterEnvironment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableCouchbaseRepositories(basePackages = {"com.springboot.couchbase.springbootrealworld"})
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    public static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    @Autowired
    private ClusterProperties clusterProperties;
    @Autowired
    private ObjectMapper objectMapper;

    public String getConnectionString() {
        return clusterProperties.getConnectionString();
    }

    public String getUserName() {
        return clusterProperties.getUsername();
    }

    public String getPassword() {
        return clusterProperties.getPassword();
    }

    public String getBucketName() {
        return clusterProperties.getDefaultBucket();
    }

    @Override
    protected String getScopeName() {
        if (clusterProperties.getDefaultScope() != null)
            return clusterProperties.getDefaultScope();
        else return null;
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Override
    protected void configureEnvironment(final ClusterEnvironment.Builder builder) {
        if (clusterProperties.isUseCapella()) builder.applyProfile("wan-development").securityConfig().enableTls(true);
    }


}