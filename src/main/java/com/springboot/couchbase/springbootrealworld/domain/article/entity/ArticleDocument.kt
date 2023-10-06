package com.springboot.couchbase.springbootrealworld.domain.article.entity

import com.springboot.couchbase.springbootrealworld.domain.common.entiity.BaseDocument
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import lombok.experimental.SuperBuilder
import org.springframework.data.couchbase.core.mapping.Document
import org.springframework.data.couchbase.core.mapping.Field

@Document
data class ArticleDocument(
        @Field
        var slug: String?,
        @Field
        var title: String?,
        @Field
        var description: String?,
        @Field
        var body: String?,
        @Field
        var tagList: List<String>?,
        var favoriteList: List<FavoriteDocument>?,
        @Field
        var author: UserDocument?
) : BaseDocument() {

    companion object {
        const val ARTICLE_COLLECTION_NAME = "article"
    }

    constructor(
            slug: String?,
            title: String?,
            description: String?,
            body: String?,
            taglist: List<String>?,
            author: UserDocument?
    ) : this(slug, title, description, body, taglist, null, author)
}
