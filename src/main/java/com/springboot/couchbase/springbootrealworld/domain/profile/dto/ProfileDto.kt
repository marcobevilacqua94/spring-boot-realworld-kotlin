package com.springboot.couchbase.springbootrealworld.domain.profile.dto

data class ProfileDto(
        val username: String,
        val bio: String?,
        val image: String?,
        val following: Boolean = false
) {
    data class Single(val profile: ProfileDto)
}
