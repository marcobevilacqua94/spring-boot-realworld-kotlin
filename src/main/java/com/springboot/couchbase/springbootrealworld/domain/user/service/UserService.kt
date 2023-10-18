package com.springboot.couchbase.springbootrealworld.domain.user.service

import com.springboot.couchbase.springbootrealworld.domain.user.dto.Login
import com.springboot.couchbase.springbootrealworld.domain.user.dto.Registration
import com.springboot.couchbase.springbootrealworld.domain.user.dto.Update
import com.springboot.couchbase.springbootrealworld.domain.user.dto.UserDto
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails

interface UserService {

    fun registration(registration: Registration): UserDto

    fun login(login: Login): UserDto

    fun currentUser(authUserDetails: AuthUserDetails): UserDto

    fun update(update: Update, authUserDetails: AuthUserDetails): UserDto
}
