package com.springboot.couchbase.springbootrealworld.domain.profile.controller

import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto.Single
import com.springboot.couchbase.springbootrealworld.domain.profile.service.ProfileService
import com.springboot.couchbase.springbootrealworld.exception.AppException
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
class ProfilesController @Autowired constructor(private val profileService: ProfileService) {

    @GetMapping("/{username}")
    fun getProfile(@PathVariable("username") name: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails): Single {
        try {
            return Single(profileService.getProfile(name, authUserDetails))
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    @PostMapping("/{username}/follow")
    fun followUser(@PathVariable("username") name: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails): Single {
        try {
            return Single(profileService.followUser(name, authUserDetails))
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    @DeleteMapping("/{username}/follow")
    fun unfollowUser(@PathVariable("username") name: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails): Single {
        try {
            return Single(profileService.unfollowUser(name, authUserDetails))
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }
}
