package com.springboot.couchbase.springbootrealworld.domain.user.controller

import com.springboot.couchbase.springbootrealworld.domain.user.dto.Update
import com.springboot.couchbase.springbootrealworld.domain.user.dto.UserDto
import com.springboot.couchbase.springbootrealworld.domain.user.service.UserService
import com.springboot.couchbase.springbootrealworld.exception.AppException
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {

    @GetMapping
    fun currentUser(@AuthenticationPrincipal authUserDetails: AuthUserDetails): UserDto {
        try {
            return userService.currentUser(authUserDetails)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    @PutMapping
    fun update(@Valid @RequestBody update: Update, @AuthenticationPrincipal authUserDetails: AuthUserDetails): UserDto {
        try {
            return userService.update(update, authUserDetails)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }
}
