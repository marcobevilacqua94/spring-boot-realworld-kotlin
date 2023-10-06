package com.springboot.couchbase.springbootrealworld.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import javax.servlet.FilterChain
import javax.servlet.GenericFilter
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class JWTAuthFilter @Autowired constructor(
        private val jwtUtils: JwtUtils,
        private val authenticationProvider: AuthenticationProvider
) : GenericFilter() {

    companion object {
        const val TOKEN_PREFIX = "Token "
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as HttpServletRequest
        httpRequest.getHeader(HttpHeaders.AUTHORIZATION)?.let { authHeader ->
            if (authHeader.startsWith(TOKEN_PREFIX)) {
                val token = authHeader.substring(TOKEN_PREFIX.length)
                if (jwtUtils.validateToken(token)) {
                    val sub = jwtUtils.getSub(token)
                    authenticationProvider.getAuthentication(sub)?.let {
                        SecurityContextHolder.getContext().authentication = it
                    }
                }
            }
        }
        chain?.doFilter(request, response)
    }
}
