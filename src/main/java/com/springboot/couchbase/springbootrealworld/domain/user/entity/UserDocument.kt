package com.springboot.couchbase.springbootrealworld.domain.user.entity

import com.springboot.couchbase.springbootrealworld.domain.common.entiity.BaseDocument
import org.springframework.data.couchbase.core.mapping.Document
import org.springframework.data.couchbase.core.mapping.Field

@Document
data class UserDocument(
        @Field val username: String?,
        @Field val email: String?,
        @Field val password: String?,
        @Field val bio: String?,
        @Field val image: String?
) : BaseDocument() {

    companion object {
        const val USER_COLLECTION_NAME = "user"
    }
}
