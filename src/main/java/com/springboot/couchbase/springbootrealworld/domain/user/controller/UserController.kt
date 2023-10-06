package com.springboot.couchbase.springbootrealworld.domain.user.controller;

import com.springboot.couchbase.springbootrealworld.domain.user.dto.UserDto;
import com.springboot.couchbase.springbootrealworld.domain.user.service.UserService;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserDto currentUser(@AuthenticationPrincipal AuthUserDetails authUserDetails) {
        // TODO: userService.getUser(userId: String) 로 변경
        try {
            return userService.currentUser(authUserDetails);
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserDto.Update update, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return userService.update(update, authUserDetails);
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }
}
