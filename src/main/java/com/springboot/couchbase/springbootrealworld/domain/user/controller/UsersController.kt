package com.springboot.couchbase.springbootrealworld.domain.user.controller

import com.springboot.couchbase.springbootrealworld.domain.user.dto.UserDto
import com.springboot.couchbase.springbootrealworld.domain.user.service.UserService
import com.springboot.couchbase.springbootrealworld.exception.AppException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UsersController(@Autowired private val userService: UserService) {

    @PostMapping
    fun registration(@RequestBody @Valid registration: UserDto.Registration): UserDto {
        try {
            return userService.registration(registration)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody login: UserDto.Login): UserDto {
        try {
            return userService.login(login)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }
}
