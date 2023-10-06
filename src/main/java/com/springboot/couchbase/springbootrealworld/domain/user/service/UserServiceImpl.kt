package com.springboot.couchbase.springbootrealworld.domain.user.service;

import com.springboot.couchbase.springbootrealworld.domain.user.dto.UserDto;
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument;
import com.springboot.couchbase.springbootrealworld.domain.user.repository.UserRepository;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import com.springboot.couchbase.springbootrealworld.exception.Error;
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails;
import com.springboot.couchbase.springbootrealworld.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public UserDto registration(final UserDto.Registration registration) {
        userRepository.findByUsernameOrEmail(registration.getUsername(), registration.getEmail()).stream().findAny().ifPresent(entity -> {
            throw new AppException(Error.DUPLICATED_USER);
        });
        UserDocument userEntity = UserDocument.builder()
                .username(registration.getUsername())
                .email(registration.getEmail())
                .password(passwordEncoder.encode(registration.getPassword()))
                .bio("")
                .build();
        userRepository.save(userEntity);
        return convertEntityToDto(userEntity);
    }


    @Transactional(readOnly = true)
    @Override
    public UserDto login(UserDto.Login login) {
        UserDocument userDocument = userRepository.findByEmail(login.getEmail()).filter(user -> passwordEncoder.matches(login.getPassword(), user.getPassword())).orElseThrow(() -> new AppException(Error.LOGIN_INFO_INVALID));
        System.out.println("Login : " + login.getEmail());
        return convertEntityToDto(userDocument);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto currentUser(AuthUserDetails authUserDetails) {
        UserDocument userEntity = userRepository.findByEmail(authUserDetails.getEmail()).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        return convertEntityToDto(userEntity);
    }

    @Override
    public UserDto update(UserDto.Update update, AuthUserDetails authUserDetails) {
        UserDocument userDocument = userRepository.findByEmail(authUserDetails.getEmail()).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));

        if (update.getUsername() != null) {
            userRepository.findByUsername(update.getUsername())
                    .filter(found -> !found.getId().equals(userDocument.getId()))
                    .ifPresent(found -> {
                        throw new AppException(Error.DUPLICATED_USER);
                    });
            userDocument.setUsername(update.getUsername());
        }

        if (update.getEmail() != null) {
            userRepository.findByEmail(update.getEmail())
                    .filter(found -> !found.getId().equals(userDocument.getId()))
                    .ifPresent(found -> {
                        throw new AppException(Error.DUPLICATED_USER);
                    });
            userDocument.setEmail(update.getEmail());
        }


        if (update.getBio() != null) {
            userDocument.setBio(update.getBio());
        }

        if (update.getImage() != null) {
            userDocument.setImage(update.getImage());
        }

        userRepository.save(userDocument);
        return convertEntityToDto(userDocument);
    }


    private UserDto convertEntityToDto(UserDocument userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .password(userEntity.getPassword())
                .username(userEntity.getUsername())
                .bio(userEntity.getBio())
                .email(userEntity.getEmail())
                .image(userEntity.getImage())
                .token(jwtUtils.encode(userEntity.getEmail()))
                .build();
    }


}
