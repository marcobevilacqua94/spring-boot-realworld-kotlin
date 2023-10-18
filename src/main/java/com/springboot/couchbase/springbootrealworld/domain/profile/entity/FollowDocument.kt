package com.springboot.couchbase.springbootrealworld.domain.profile.entity

import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.couchbase.core.mapping.Document
import org.springframework.data.couchbase.core.mapping.Field
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy

@Document
data class FollowDocument(

        @Id
        @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
        val id: String? = "",

        @Field
        val followee: UserDocument?,

        @Field
        val follower: UserDocument?,

        @Field
        @Version
        val version: Long? = null,

) {
        companion object {
                const val FOLLOW_COLLECTION_NAME: String = "follow"
        }
}
