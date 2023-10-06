package com.springboot.couchbase.springbootrealworld.domain.article.entity

import com.springboot.couchbase.springbootrealworld.domain.common.entiity.BaseDocument
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import org.springframework.data.couchbase.core.mapping.Document
import org.springframework.data.couchbase.core.mapping.Field

@Document
data class CommentDocument(
        @Field
        var body: String?,
        @Field
        var author: UserDocument?,
        @Field
        var article: ArticleDocument?
) : BaseDocument() {

    companion object {
        const val COMMENT_COLLECTION_NAME = "comment"
    }
}
