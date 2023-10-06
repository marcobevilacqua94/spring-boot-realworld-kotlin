package com.springboot.couchbase.springbootrealworld.domain.user.service

import com.springboot.couchbase.springbootrealworld.domain.user.dto.UserDto
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails

interface UserService {

    fun registration(registration: UserDto.Registration): UserDto

    fun login(login: UserDto.Login): UserDto

    fun currentUser(authUserDetails: AuthUserDetails): UserDto

    fun update(update: UserDto.Update, authUserDetails: AuthUserDetails): UserDto
}
