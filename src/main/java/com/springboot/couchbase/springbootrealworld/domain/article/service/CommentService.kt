package com.springboot.couchbase.springbootrealworld.domain.article.service

import com.springboot.couchbase.springbootrealworld.domain.article.dto.CommentDto
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails

interface CommentService {

    fun addCommentsToAnArticle(slug: String, comment: CommentDto, authUserDetails: AuthUserDetails): CommentDto

    fun getCommentsBySlug(slug: String, authUserDetails: AuthUserDetails): List<CommentDto>

    fun delete(commentId: String, authUserDetails: AuthUserDetails)
}
