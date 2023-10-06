package com.springboot.couchbase.springbootrealworld.domain.article.controller

import com.springboot.couchbase.springbootrealworld.domain.article.dto.ArticleDto
import com.springboot.couchbase.springbootrealworld.domain.article.dto.CommentDto
import com.springboot.couchbase.springbootrealworld.domain.article.dto.FavoriteDto
import com.springboot.couchbase.springbootrealworld.domain.article.model.FeedParams
import com.springboot.couchbase.springbootrealworld.domain.article.service.ArticleService
import com.springboot.couchbase.springbootrealworld.domain.article.service.CommentService
import com.springboot.couchbase.springbootrealworld.domain.article.service.FavoriteService
import com.springboot.couchbase.springbootrealworld.exception.AppException
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.couchbase.core.CouchbaseTemplate
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid

@RestController
@RequestMapping("/articles")
class ArticleController {

    @Autowired
    private lateinit var articleService: ArticleService

    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var favoriteService: FavoriteService

    @Autowired
    private lateinit var couchbaseTemplate: CouchbaseTemplate

    // Adding an Article
    @PostMapping
    fun createArticle(@RequestBody article: ArticleDto.SingleArticle<ArticleDto>, @AuthenticationPrincipal authUserDetails: AuthUserDetails): ArticleDto.SingleArticle<ArticleDto> {
        try {
            return ArticleDto.SingleArticle(articleService.createArticle(article.article, authUserDetails))
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Get an Article using slug
    @GetMapping("/{slug}")
    fun getArticle(@PathVariable slug: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails): ArticleDto.SingleArticle<ArticleDto> {
        try {
            return ArticleDto.SingleArticle(articleService.getArticle(slug, authUserDetails))
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Update an Article using slug
    @PutMapping("/{slug}")
    fun updateArticle(@PathVariable slug: String, @RequestBody article: ArticleDto.SingleArticle<ArticleDto.Update>, @AuthenticationPrincipal authUserDetails: AuthUserDetails): ArticleDto.SingleArticle<ArticleDto> {
        try {
            return ArticleDto.SingleArticle(articleService.updateArticle(slug, article.article, authUserDetails))
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Delete an Article using slug
    @DeleteMapping("/{slug}")
    fun deleteArticle(@PathVariable slug: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails) {
        try {
            articleService.deleteArticle(slug, authUserDetails)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Post a comment with a slug as parameter
    @PostMapping("/{slug}/comments")
    fun addCommentsToAnArticle(
            @PathVariable slug: String,
            @RequestBody comment: CommentDto.SingleComment,
            @AuthenticationPrincipal authUserDetails: AuthUserDetails
    ): CommentDto.SingleComment {
        try {
            println(comment)
            val addedComment = commentService.addCommentsToAnArticle(slug, comment.comment, authUserDetails)
            return CommentDto.SingleComment(addedComment)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Get all comments to an article with a slug as parameter
    @GetMapping("/{slug}/comments")
    fun getCommentsFromAnArticle(@PathVariable slug: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails): CommentDto.MultipleComments {
        try {
            return CommentDto.MultipleComments.builder()
                    .comments(commentService.getCommentsBySlug(slug, authUserDetails))
                    .build()
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Delete a comment to an article with a comment id as parameter
    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(@PathVariable("commentId") commentId: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails) {
        try {
            commentService.delete(commentId, authUserDetails)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Post a favorite article with a slug as a parameter
    @PostMapping("/{slug}/favorite")
    fun favoriteArticle(@PathVariable slug: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails): ArticleDto.SingleArticle<ArticleDto> {
        try {
            return ArticleDto.SingleArticle(articleService.favoriteArticle(slug, authUserDetails))
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Get a favorite article with a slug as a parameter
    @GetMapping("/{slug}/favorites")
    fun getFavoritesFromAnArticle(@PathVariable slug: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails): FavoriteDto.MultipleFavorites {
        try {
            return FavoriteDto.MultipleFavorites.builder()
                    .favorites(favoriteService.getFavoritesBySlug(slug, authUserDetails))
                    .build()
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Delete a favorite article with a slug as a parameter
    @DeleteMapping("/{slug}/favorite")
    fun deleteFavorite(@PathVariable("slug") slug: String, @AuthenticationPrincipal authUserDetails: AuthUserDetails): ArticleDto.SingleArticle<ArticleDto> {
        try {
            return ArticleDto.SingleArticle(favoriteService.delete(slug, authUserDetails))
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    // Get all article with pagination and user authentication
    @GetMapping("/all")
    fun feedArticles(@ModelAttribute @Valid feedParams: FeedParams, @AuthenticationPrincipal authUserDetails: AuthUserDetails): ArticleDto.MultipleArticle {
        try {
            val articles = articleService.feedArticles(authUserDetails, feedParams)
            return ArticleDto.MultipleArticle(articles, articles.size)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    @GetMapping("/feed")
    fun getArticles(@AuthenticationPrincipal authUserDetails: AuthUserDetails): ArticleDto.MultipleArticle {
        try {
            val articles = articleService.getAllArticles(authUserDetails)
            return ArticleDto.MultipleArticle(articles, articles.size)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }

    @GetMapping
    fun getArticlesYouFollow(): ArticleDto.MultipleArticle {
        try {
            val articles = articleService.getAllArticlesYouFollow()
            return ArticleDto.MultipleArticle(articles, articles.size)
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }
}
