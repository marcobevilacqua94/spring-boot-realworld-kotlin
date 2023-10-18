package com.springboot.couchbase.springbootrealworld.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*

@Component
class JWTAuthFilter @Autowired constructor(
    private val jwtUtils: JwtUtils,
    private val authenticationProvider: AuthenticationProvider
) : Filter {

    companion object {
        const val TOKEN_PREFIX = "Token "
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val authHeader = (request as? HttpServletRequest)?.getHeader(HttpHeaders.AUTHORIZATION)
        authHeader?.takeIf { it.startsWith(TOKEN_PREFIX) }
            ?.substring(TOKEN_PREFIX.length)
            ?.takeIf(jwtUtils::validateToken)
            ?.let(jwtUtils::getSub)
            ?.let(authenticationProvider::getAuthentication)
            ?.also { SecurityContextHolder.getContext().authentication = it }

        chain?.doFilter(request, response)
    }
}
