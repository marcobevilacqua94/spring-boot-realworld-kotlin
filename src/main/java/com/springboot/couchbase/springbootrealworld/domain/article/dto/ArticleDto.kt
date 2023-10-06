package com.springboot.couchbase.springbootrealworld.domain.article.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto
import lombok.Builder
import java.util.*

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
        var author: ProfileDto?
) {
    data class SingleArticle<T>(
            var article: T?
    )

    data class MultipleArticle(
            var articles: List<ArticleDto>?,
            var articlesCount: Int
    )

    data class TagList(
            var tags: List<String>?
    )

    data class Update(
            var title: String?,
            var description: String?,
            var body: String?
    )
}
