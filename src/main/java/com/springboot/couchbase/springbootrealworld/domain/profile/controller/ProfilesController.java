package com.springboot.couchbase.springbootrealworld.domain.profile.controller;


import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto.Single;
import com.springboot.couchbase.springbootrealworld.domain.profile.service.ProfileService;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfilesController {
    @Autowired
    private final ProfileService profileService;

    //Get profile of the login user using username as parameter
    @GetMapping("/{username}")
    public Single getProfile(@PathVariable("username") String name, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return new Single(profileService.getProfile(name, authUserDetails));
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //following a user
    @PostMapping("/{username}/follow")
    public Single followUser(@PathVariable("username") String name, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return new Single(profileService.followUser(name, authUserDetails));
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //unfollow a user
    @DeleteMapping("/{username}/follow")
    public Single unfollowUser(@PathVariable("username") String name, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return new Single(profileService.unfollowUser(name, authUserDetails));
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

}
