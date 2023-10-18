package com.springboot.couchbase.springbootrealworld.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.Instant
import java.util.Date

open class JwtUtils(signKey: String, private val validSeconds: Long) {
    private val key: Key = Keys.hmacShaKeyFor(signKey.toByteArray(StandardCharsets.UTF_8))

    fun encode(sub: String?): String? {
        if (sub.isNullOrEmpty()) {
            return null
        }
        val exp = Instant.now()
        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(Date(exp.toEpochMilli()))
                .setExpiration(Date(exp.toEpochMilli() + validSeconds * 1000))
                .signWith(key)
                .compact()
    }

    fun validateToken(jwt: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).body
            val now = Instant.now()
            val exp = claims.expiration
            exp.after(Date.from(now))
        } catch (e: JwtException) {
            false
        }
    }

    fun getSub(jwt: String): String? {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).body
            claims.subject
        } catch (e: JwtException) {
            null
        }
    }
}
