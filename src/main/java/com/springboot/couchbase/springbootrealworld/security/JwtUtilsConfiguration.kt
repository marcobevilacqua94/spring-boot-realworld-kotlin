package com.springboot.couchbase.springbootrealworld.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class JwtUtilsConfiguration {

    @Bean
    open fun jwtUtils(
            @Value("\${realworld.auth.token.sign-key}") signKey: String,
            @Value("\${realworld.auth.token.valid-time}") validTime: Long
    ): JwtUtils {
        if (signKey.length < 32) {
            throw Exception("signKey must have length at least 32")
        }
        return JwtUtils(signKey, validTime)
    }
}
