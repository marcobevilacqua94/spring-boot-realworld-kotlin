package com.springboot.couchbase.springbootrealworld.domain.article.service

import com.springboot.couchbase.springbootrealworld.domain.article.dto.CommentDto
import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument
import com.springboot.couchbase.springbootrealworld.domain.article.entity.CommentDocument
import com.springboot.couchbase.springbootrealworld.domain.article.repository.ArticleRepository
import com.springboot.couchbase.springbootrealworld.domain.article.repository.CommentRepository
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto
import com.springboot.couchbase.springbootrealworld.domain.profile.service.ProfileService
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import com.springboot.couchbase.springbootrealworld.exception.AppException
import com.springboot.couchbase.springbootrealworld.exception.Error
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CommentServiceImpl @Autowired constructor(
        private val articleRepository: ArticleRepository,
        private val commentRepository: CommentRepository,
        private val profileService: ProfileService
) : CommentService {

    override fun addCommentsToAnArticle(slug: String, comment: CommentDto, authUserDetails: AuthUserDetails): CommentDto {
        val articleDocument: ArticleDocument? = articleRepository.findBySlug(slug)
        val commentDocument = CommentDocument.builder()
                .id(comment.id)
                .body(comment.body)
                .author(UserDocument.builder().id(authUserDetails.id).build())
                .article(articleDocument)
                .createdAt(Date())
                .updatedAt(Date())
                .build()
        commentRepository.save(commentDocument)
        return convertToDTO(authUserDetails, commentDocument)
    }

    override fun getCommentsBySlug(slug: String, authUserDetails: AuthUserDetails): List<CommentDto> {
        val article = articleRepository.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        val articleId = article.id
        val commentEntities = commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId)
        return commentEntities.map { convertToDTO(authUserDetails, it) }
    }

    override fun delete(commentId: String, authUserDetails: AuthUserDetails) {
        val commentEntity: Optional<CommentDocument> = commentRepository.findById(commentId)
        commentEntity.ifPresentOrElse(
                { commentRepository.delete(it) },
                { throw AppException(Error.COMMENT_NOT_FOUND) }
        )
    }

    private fun convertToDTO(authUserDetails: AuthUserDetails?, commentDocument: CommentDocument): CommentDto {
        val builder = CommentDto.builder()
                .id(commentDocument.id)
                .createdAt(commentDocument.createdAt)
                .updatedAt(commentDocument.updatedAt)
                .body(commentDocument.body)
        if (authUserDetails != null) {
            val author: ProfileDto = profileService.getProfileByUserId(commentDocument.author.email, authUserDetails)
            builder.author(author)
        }
        return builder.build()
    }

    private fun convertToDTOs(commentDocument: CommentDocument): CommentDto {
        return convertToDTO(null, commentDocument)
    }
}
