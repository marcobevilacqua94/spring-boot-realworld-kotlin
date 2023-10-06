package com.springboot.couchbase.springbootrealworld.security

import com.springboot.couchbase.springbootrealworld.domain.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails? {
        return userRepository.findByEmail(email)
                .map { userEntity ->
                    AuthUserDetails.builder()
                            .id(userEntity.id)
                            .email(userEntity.email)
                            .build()
                }
                .orElse(null)
    }
}
