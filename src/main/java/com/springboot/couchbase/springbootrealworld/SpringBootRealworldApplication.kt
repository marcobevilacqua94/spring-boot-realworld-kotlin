package com.springboot.couchbase.springbootrealworld

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.bind.annotation.CrossOrigin

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableTransactionManagement
class SpringBootRealworldApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringBootRealworldApplication::class.java, *args)
}
