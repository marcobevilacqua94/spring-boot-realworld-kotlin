package com.springboot.couchbase.springbootrealworld.domain.article.entity

import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.couchbase.core.mapping.Document
import org.springframework.data.couchbase.core.mapping.Field
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy

@Document
data class FavoriteDocument(

        @Id
        @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
        var id: String? = null,
        @Field
        var author: UserDocument?,
        @Field
        var article: ArticleDocument?,
        @Field
        @Version
        var version: Long? = null
) {

    companion object {
        const val FAVORITE_COLLECTION_NAME = "favorite"
    }
}
