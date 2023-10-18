package com.springboot.couchbase.springbootrealworld.domain.article.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.springboot.couchbase.springbootrealworld.configuration.CouchbaseConfig
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor
import lombok.ToString
import java.util.*



@NoArgsConstructor
@AllArgsConstructor
@ToString
data class ArticleDto(
        var id: String?,
        var slug: String?,
        var title: String?,
        var description: String?,
        var body: String?,
        var tagList: List<String>?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
        var createdAt: Date?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
        var updatedAt: Date?,

        var favorited: Boolean?,
        var favoritesCount: Long?,
        var author: ProfileDto? = null
) {

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    class SingleArticleDto(
            @JsonProperty("article")
            var article: ArticleDto
    )


    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
//    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    class MultipleArticle(
            @JsonProperty("articles")
            var articles: List<ArticleDto>?,
            @JsonProperty("articlesCount")
            var articlesCount: Int
    )


    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    class TagList(
        @JsonProperty("tags")
            var tags: List<String>?
    )


    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonTypeName("article")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    class Update(
            var title: String,
            var description: String?,
            var body: String?
    )
}
