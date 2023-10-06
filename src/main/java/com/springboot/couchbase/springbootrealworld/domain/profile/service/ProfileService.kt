package com.springboot.couchbase.springbootrealworld.domain.profile.service

import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails

interface ProfileService {
    fun getProfile(username: String, authUserDetails: AuthUserDetails): ProfileDto
    fun followUser(name: String, authUserDetails: AuthUserDetails): ProfileDto
    fun unfollowUser(name: String, authUserDetails: AuthUserDetails): ProfileDto
    fun getProfileByUserId(userId: String, authUserDetails: AuthUserDetails): ProfileDto
    fun getProfileByUserIds(userId: String): ProfileDto
}
