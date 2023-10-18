package com.springboot.couchbase.springbootrealworld.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthUserDetails(
        val id: String,
        val email: String,
        val bio: String = ""
) : UserDetails {

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
