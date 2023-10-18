package com.springboot.couchbase.springbootrealworld.domain.article.entity

import com.springboot.couchbase.springbootrealworld.domain.common.entiity.BaseDocument
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.couchbase.core.mapping.Document
import org.springframework.data.couchbase.core.mapping.Field
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy
import org.springframework.data.couchbase.core.mapping.id.IdAttribute
import java.util.*

@Document
data class CommentDocument(

    @Field
        var body: String?,
    @Field
        var author: UserDocument?,
    @Field
        var article: ArticleDocument?,

        @Id
        @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
        @IdAttribute
        var id: String? = null,

        @Field
        var createdAt: Date = Date(),

        @Field
        var updatedAt: Date = Date(),

        @Field
        @Version
        private var version: Long = 0
) {

    companion object {
        const val COMMENT_COLLECTION_NAME = "comment"
    }
}
