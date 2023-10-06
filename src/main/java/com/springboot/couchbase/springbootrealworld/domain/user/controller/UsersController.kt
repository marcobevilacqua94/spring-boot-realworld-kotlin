package com.springboot.couchbase.springbootrealworld.domain.user.controller;

import com.springboot.couchbase.springbootrealworld.domain.user.dto.UserDto;
import com.springboot.couchbase.springbootrealworld.domain.user.service.UserService;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserService userService;


    @PostMapping
    public UserDto registration(@RequestBody @Valid UserDto.Registration registration) {
        try {
            return userService.registration(registration);
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }


    @PostMapping("/login")
    public UserDto login(@RequestBody UserDto.Login login) {
        try {
            return userService.login(login);
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

}

