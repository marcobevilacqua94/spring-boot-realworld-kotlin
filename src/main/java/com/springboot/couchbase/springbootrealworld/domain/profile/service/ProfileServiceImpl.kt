package com.springboot.couchbase.springbootrealworld.domain.profile.service;


import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto;
import com.springboot.couchbase.springbootrealworld.domain.profile.entity.FollowDocument;
import com.springboot.couchbase.springbootrealworld.domain.profile.repository.FollowRepository;
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument;
import com.springboot.couchbase.springbootrealworld.domain.user.repository.UserRepository;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import com.springboot.couchbase.springbootrealworld.exception.Error;
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final FollowRepository followRepository;


    @Override
    public ProfileDto getProfile(String name, AuthUserDetails authUserDetails) {
        UserDocument user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        Boolean following = followRepository.findByFolloweeIdAndFollowerId(user.getId(), String.valueOf(authUserDetails.getId())).isPresent();

        return convertToProfile(user, following);
    }

    @Transactional
    @Override
    public ProfileDto followUser(String name, AuthUserDetails authUserDetails) {
        UserDocument followee = userRepository.findByUsername(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        UserDocument follower = userRepository.findById(String.valueOf(authUserDetails.getId())).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        ; // myself

        followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .ifPresent(follow -> {
                    throw new AppException(Error.ALREADY_FOLLOWED_USER);
                });

        FollowDocument follow = FollowDocument.builder().followee(followee).follower(follower).build();
        followRepository.save(follow);

        return convertToProfile(followee, true);
    }

    @Transactional
    @Override
    public ProfileDto unfollowUser(String name, AuthUserDetails authUserDetails) {
        UserDocument followee = userRepository.findByUsername(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        UserDocument follower = userRepository.findById(String.valueOf(authUserDetails.getId())).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        ; // myself

        FollowDocument follow = followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .orElseThrow(() -> new AppException(Error.FOLLOW_NOT_FOUND));
        System.out.println(follow);
        followRepository.delete(follow);

        return convertToProfile(followee, false);
    }

    @Override
    public ProfileDto getProfileByUserId(String userId, AuthUserDetails authUserDetails) {
        UserDocument user = userRepository.findByEmail(authUserDetails.getEmail()).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
//        UserDocument user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        Boolean following = followRepository.findByFolloweeIdAndFollowerId(user.getId(), String.valueOf(authUserDetails.getId())).isPresent();
        return convertToProfile(user, following);
    }


    private ProfileDto convertToProfile(UserDocument user, Boolean following) {
        return ProfileDto.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(following)
                .build();
    }

    @Override
    public ProfileDto getProfileByUserIds(String userId) {
        UserDocument user = userRepository.findById(userId).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        return convertToProfiles(user);
    }

    private ProfileDto convertToProfiles(UserDocument user) {
        return ProfileDto.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }
}
