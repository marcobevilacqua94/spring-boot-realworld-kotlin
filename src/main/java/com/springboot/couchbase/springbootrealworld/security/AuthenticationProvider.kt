package com.springboot.couchbase.springbootrealworld.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class AuthenticationProvider(private val userDetailsService: UserDetailsService) {

    fun getAuthentication(username: String?): Authentication? {
        return username?.let {
            userDetailsService.loadUserByUsername(it)?.let { userDetails ->
                UsernamePasswordAuthenticationToken(userDetails, userDetails.password, userDetails.authorities)
            }
        }
    }
}
