package com.springboot.couchbase.springbootrealworld.domain.user.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
data class UserDto(
        val id: String,
        val email: String,
        val token: String,
        val username: String,
        val password: String,
        val bio: String?,
        val image: String?
)

@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
data class Registration(
        val username: String,
        val email: String,
        val password: String
)

@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
data class Login(
        val email: String,
        val password: String
)

@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
data class Update(
        val id: String,
        val email: String,
        val username: String,
        val bio: String?,
        val image: String?
)
