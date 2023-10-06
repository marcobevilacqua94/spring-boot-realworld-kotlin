package com.springboot.couchbase.springbootrealworld.domain.article.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto
import java.util.*

data class CommentDto(
        var id: String?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
        var createdAt: Date?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
        var updatedAt: Date?,

        var body: String?,
        var author: ProfileDto?
) {
    data class SingleComment(
            var comment: CommentDto?
    )

    data class MultipleComments(
            var comments: List<CommentDto>?
    )
}