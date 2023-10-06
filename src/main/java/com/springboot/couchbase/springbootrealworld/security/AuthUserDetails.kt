package com.springboot.couchbase.springbootrealworld.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthUserDetails(
        private val id: String,
        val email: String,
        private val bio: String
) : UserDetails {

    override fun getId(): String {
        return id
    }

    override fun getEmail(): String {
        return email
    }

    override fun getBio(): String {
        return bio
    }

    override fun getAuthorities(): Collection<GrantedAuthority>? {
        // no authority in this project
        return null
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
