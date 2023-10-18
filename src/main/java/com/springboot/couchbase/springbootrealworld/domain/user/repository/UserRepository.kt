package com.springboot.couchbase.springbootrealworld.domain.user.repository

import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import org.springframework.data.couchbase.repository.Collection
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@Collection(UserDocument.USER_COLLECTION_NAME)
interface UserRepository : CrudRepository<UserDocument, String> {

    fun findByUsernameOrEmail(username: String, email: String): List<UserDocument>

    fun findByEmail(email: String): Optional<UserDocument>

    override fun findById(id: String): Optional<UserDocument>

    fun findByUsername(username: String): Optional<UserDocument>

}
