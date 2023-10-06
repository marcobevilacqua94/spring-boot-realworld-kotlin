import com.couchbase.client.core.env.SecurityConfig
import com.couchbase.client.java.Cluster
import com.couchbase.client.java.env.ClusterEnvironment
import com.fasterxml.jackson.databind.ObjectMapper
import com.springboot.couchbase.springbootrealworld.configuration.ClusterProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories

@Configuration
@EnableCouchbaseRepositories(basePackages = ["com.springboot.couchbase.springbootrealworld"])
class CouchbaseConfig : AbstractCouchbaseConfiguration() {

    @Autowired
    private lateinit var clusterProperties: ClusterProperties

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun getConnectionString(): String {
        return clusterProperties.connectionString ?: ""
    }

    override fun getUserName(): String {
        return clusterProperties.username ?: ""
    }

    override fun getPassword(): String {
        return clusterProperties.password ?: ""
    }

    override fun getBucketName(): String {
        return clusterProperties.defaultBucket ?: ""
    }

    override fun getScopeName(): String? {
        return clusterProperties.defaultScope
    }

    override fun autoIndexCreation(): Boolean {
        return true
    }

    override fun configureEnvironment(builder: ClusterEnvironment.Builder) {
        if (clusterProperties.useCapella) {
            builder.applyProfile("wan-development").securityConfig().enableTls(true)
        }
    }

    companion object {
        const val ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    }
}