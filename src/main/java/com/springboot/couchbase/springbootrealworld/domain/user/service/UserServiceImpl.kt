package com.springboot.couchbase.springbootrealworld.domain.user.service

import com.springboot.couchbase.springbootrealworld.domain.user.dto.Login
import com.springboot.couchbase.springbootrealworld.domain.user.dto.Registration
import com.springboot.couchbase.springbootrealworld.domain.user.dto.Update
import com.springboot.couchbase.springbootrealworld.domain.user.dto.UserDto
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import com.springboot.couchbase.springbootrealworld.domain.user.repository.UserRepository
import com.springboot.couchbase.springbootrealworld.exception.AppException
import com.springboot.couchbase.springbootrealworld.exception.Error
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails
import com.springboot.couchbase.springbootrealworld.security.JwtUtils
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
open class UserServiceImpl @Autowired constructor(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtUtils: JwtUtils
) : UserService {

    override fun registration(registration: Registration): UserDto {
        userRepository.findByUsernameOrEmail(registration.username, registration.email)
            .stream()
                .findAny()
                .ifPresent {
                    throw AppException(Error.DUPLICATED_USER)
                }

        val userEntity = UserDocument(username = registration.username, email = registration.email, password = passwordEncoder.encode(registration.password))

        userRepository.save(userEntity)
        return convertEntityToDto(userEntity)
    }

    //@Transactional(readOnly = true)
    override fun login(login: Login): UserDto {
        val userDocument = userRepository.findByEmail(login.email)
                .filter { user -> passwordEncoder.matches(login.password, user.password) }
                .orElseThrow { AppException(Error.LOGIN_INFO_INVALID) }

        println("Login : " + login.email)
        return convertEntityToDto(userDocument)
    }

   // @Transactional(readOnly = true)
    override fun currentUser(authUserDetails: AuthUserDetails): UserDto {
        val userEntity = userRepository.findByEmail(authUserDetails.email)
                .orElseThrow { AppException(Error.USER_NOT_FOUND) }
        return convertEntityToDto(userEntity)
    }

    override fun update(update: Update, authUserDetails: AuthUserDetails): UserDto {
        val userDocument = userRepository.findByEmail(authUserDetails.email)
                .orElseThrow { AppException(Error.USER_NOT_FOUND) }

        update.username?.let { newUsername ->
            userRepository.findByUsername(newUsername)
                    .filter { found -> !found.id.equals(userDocument.id) }
                    .ifPresent {
                        throw AppException(Error.DUPLICATED_USER)
                    }
            userDocument.username = newUsername
        }

        update.email?.let { newEmail ->
            userRepository.findByEmail(newEmail)
                    .filter { found -> !found.id.equals(userDocument.id) }
                    .ifPresent {
                        throw AppException(Error.DUPLICATED_USER)
                    }
            userDocument.email = newEmail
        }

        update.bio?.let { userDocument.bio = it }
        update.image?.let { userDocument.image = it }

        userRepository.save(userDocument)
        return convertEntityToDto(userDocument)
    }

    private fun convertEntityToDto(userEntity: UserDocument): UserDto {
        return UserDto(id = userEntity.id!!, password = userEntity.password!!, username = userEntity.username!!, bio  = userEntity.bio, email = userEntity.email!!, image =  userEntity.image, token = jwtUtils.encode(userEntity.email)!!)
    }
}
