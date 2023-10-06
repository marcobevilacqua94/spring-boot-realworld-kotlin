package com.springboot.couchbase.springbootrealworld.domain.profile.service

import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto
import com.springboot.couchbase.springbootrealworld.domain.profile.entity.FollowDocument
import com.springboot.couchbase.springbootrealworld.domain.profile.repository.FollowRepository
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import com.springboot.couchbase.springbootrealworld.domain.user.repository.UserRepository
import com.springboot.couchbase.springbootrealworld.exception.AppException
import com.springboot.couchbase.springbootrealworld.exception.Error
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileServiceImpl @Autowired constructor(
        private val userRepository: UserRepository,
        private val followRepository: FollowRepository
) : ProfileService {

    override fun getProfile(name: String, authUserDetails: AuthUserDetails): ProfileDto {
        val user = userRepository.findByUsername(name).orElseThrow { AppException(Error.USER_NOT_FOUND) }
        val following = followRepository.findByFolloweeIdAndFollowerId(user.id, authUserDetails.id.toString()).isPresent
        return convertToProfile(user, following)
    }

    override fun followUser(name: String, authUserDetails: AuthUserDetails): ProfileDto {
        val followee = userRepository.findByUsername(name).orElseThrow { AppException(Error.USER_NOT_FOUND) }
        val follower = userRepository.findById(authUserDetails.id.toString()).orElseThrow { AppException(Error.USER_NOT_FOUND) }

        followRepository.findByFolloweeIdAndFollowerId(followee.id, follower.id)
                .ifPresent { throw AppException(Error.ALREADY_FOLLOWED_USER) }

        val follow = FollowDocument.builder().followee(followee).follower(follower).build()
        followRepository.save(follow)

        return convertToProfile(followee, true)
    }

    @Transactional
    override fun unfollowUser(name: String, authUserDetails: AuthUserDetails): ProfileDto {
        val followee = userRepository.findByUsername(name).orElseThrow { AppException(Error.USER_NOT_FOUND) }
        val follower = userRepository.findById(authUserDetails.id.toString()).orElseThrow { AppException(Error.USER_NOT_FOUND) }

        val follow = followRepository.findByFolloweeIdAndFollowerId(followee.id, follower.id)
                .orElseThrow { AppException(Error.FOLLOW_NOT_FOUND) }

        followRepository.delete(follow)

        return convertToProfile(followee, false)
    }

    override fun getProfileByUserId(userId: String, authUserDetails: AuthUserDetails): ProfileDto {
        val user = userRepository.findByEmail(authUserDetails.email).orElseThrow { AppException(Error.USER_NOT_FOUND) }
        val following = followRepository.findByFolloweeIdAndFollowerId(user.id, authUserDetails.id.toString()).isPresent
        return convertToProfile(user, following)
    }

    private fun convertToProfile(user: UserDocument, following: Boolean): ProfileDto {
        return ProfileDto.builder()
                .username(user.username)
                .bio(user.bio)
                .image(user.image)
                .following(following)
                .build()
    }

    override fun getProfileByUserIds(userId: String): ProfileDto {
        val user = userRepository.findById(userId).orElseThrow { AppException(Error.USER_NOT_FOUND) }
        return convertToProfiles(user)
    }

    private fun convertToProfiles(user: UserDocument): ProfileDto {
        return ProfileDto.builder()
                .username(user.username)
                .bio(user.bio)
                .image(user.image)
                .build()
    }
}
