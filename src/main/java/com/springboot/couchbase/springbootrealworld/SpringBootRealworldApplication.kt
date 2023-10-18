package com.springboot.couchbase.springbootrealworld

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableTransactionManagement
@ComponentScan("com.springboot.couchbase.springbootrealworld")
open class SpringBootRealworldApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringBootRealworldApplication::class.java, *args)
}
