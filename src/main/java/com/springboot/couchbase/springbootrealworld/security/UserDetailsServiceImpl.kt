package com.springboot.couchbase.springbootrealworld.security

import com.springboot.couchbase.springbootrealworld.domain.user.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
open class UserDetailsServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,

) : UserDetailsService {

//    private val userRepository: UserRepository? = null
    @Throws(UsernameNotFoundException::class)
    @Override
    //@Transactional(readOnly = true)
    override fun loadUserByUsername(email: String): UserDetails? {
        return userRepository!!.findByEmail(email)
                .map { userEntity ->
                    AuthUserDetails(userEntity.id!!, userEntity.email!!)
                }
                .orElse(null)
    }
}
