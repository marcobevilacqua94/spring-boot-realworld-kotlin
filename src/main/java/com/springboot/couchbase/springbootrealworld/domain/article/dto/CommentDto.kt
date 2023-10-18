package com.springboot.couchbase.springbootrealworld.domain.article.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.springboot.couchbase.springbootrealworld.configuration.CouchbaseConfig
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import lombok.ToString
import java.util.*

@NoArgsConstructor
@AllArgsConstructor
@ToString
data class CommentDto(
        var id: String?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
        var createdAt: Date?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
        var updatedAt: Date?,

        var body: String?,
        var author: ProfileDto? = null
) {
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    class SingleComment(
            @JsonProperty("comment")
            var comment: CommentDto
    )

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    class MultipleComments(
            @JsonProperty("comments")
            var comments: List<CommentDto>?
    )
}