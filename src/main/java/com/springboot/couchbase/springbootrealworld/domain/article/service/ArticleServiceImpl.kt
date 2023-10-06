package com.springboot.couchbase.springbootrealworld.domain.article.service;

import com.springboot.couchbase.springbootrealworld.domain.article.dto.ArticleDto;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.FavoriteDocument;
import com.springboot.couchbase.springbootrealworld.domain.article.model.FeedParams;
import com.springboot.couchbase.springbootrealworld.domain.article.repository.ArticleRepository;
import com.springboot.couchbase.springbootrealworld.domain.article.repository.CommentRepository;
import com.springboot.couchbase.springbootrealworld.domain.article.repository.FavoriteRepository;
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto;
import com.springboot.couchbase.springbootrealworld.domain.profile.entity.FollowDocument;
import com.springboot.couchbase.springbootrealworld.domain.profile.repository.FollowRepository;
import com.springboot.couchbase.springbootrealworld.domain.profile.service.ProfileService;
import com.springboot.couchbase.springbootrealworld.domain.tag.entity.ArticleTagRelationDocument;
import com.springboot.couchbase.springbootrealworld.domain.tag.repository.TagRepository;
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import com.springboot.couchbase.springbootrealworld.exception.Error;
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.query.WithConsistency;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
class ArticleServiceImpl(
        @Autowired private val articleRepository: ArticleRepository,
        @Autowired private val favoriteRepository: FavoriteRepository,
        @Autowired private val profileService: ProfileService,
        @Autowired private val followRepository: FollowRepository,
        @Autowired private val commentRepository: CommentRepository,
        @Autowired private val tagRepository: TagRepository
) : ArticleService {

    private val logger: Logger = LoggerFactory.getLogger(ArticleServiceImpl::class.java)
    @Transactional
    override fun createArticle(article: ArticleDto, authUserDetails: AuthUserDetails): ArticleDto {
        val slug = article.title!!.split(" ").joinToString("-")
        val author = UserDocument.builder().id(authUserDetails.id).build()

        val articleDocument = ArticleDocument(
                slug = slug,
                title = article.title,
                description = article.description,
                body = article.body,
                author = author,
                tagList = article.tagList?.sorted()
        )

        article.tagList?.forEach { tag ->
            tagRepository.save(ArticleTagRelationDocument.builder()
                    .article(articleDocument)
                    .tag(tag)
                    .build())
        }

        val savedArticle = articleRepository.save(articleDocument)

        return convertEntityToDto(savedArticle, false, 0L, authUserDetails)
    }


    override fun getArticleFavorite(slug: String, authUserDetails: AuthUserDetails): ArticleDto {
        val result = articleRepository.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        return convertEntityToDto(result, false, 0L, authUserDetails)
    }

    override fun getArticle(slug: String, authUserDetails: AuthUserDetails): ArticleDto {
        val result = articleRepository.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        val favoriteEntities = favoriteRepository.findAllFavorites()
        val favorited = favoriteRepository.findByArticleIdAndAuthorEmail(result.id, authUserDetails.email).isPresent
        val favoriteCount = favoriteEntities.size.toLong()
        return convertEntityToDto(result, favorited, favoriteCount, authUserDetails)
    }



    private fun convertEntityToDto(entity: ArticleDocument, favorited: Boolean, favoritesCount: Long, authUserDetails: AuthUserDetails): ArticleDto {
        val builder = ArticleDto.builder()
                .id(entity.id)
                .slug(entity.slug)
                .title(entity.title)
                .description(entity.description)
                .body(entity.body)
                .createdAt(entity.createdAt)
                .updatedAt(entity.updatedAt)
                .favorited(favorited)
                .favoritesCount(favoritesCount)

        val author = profileService.getProfileByUserIds(entity.author?.id)
        author?.let { builder.author(it) }

        entity.tagList?.let { builder.tagList(it) }

        return builder.build()
    }

    override fun updateArticle(slug: String, article: ArticleDto.Update, authUserDetails: AuthUserDetails): ArticleDto {
        val found = articleRepository.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        if (article.title != null) {
            val newSlug = article.title.split(" ").joinToString("-")
            found.title = article.title
            found.slug = newSlug
        }

        if (article.description != null) {
            found.description = article.description
        }

        if (article.body != null) {
            found.body = article.body
        }

        found.updatedAt = Date()
        articleRepository.save(found)

        return getArticle(slug, authUserDetails)
    }


    @Transactional
    override fun deleteArticle(slug: String, authUserDetails: AuthUserDetails) {
        val found = articleRepository.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        logger.info("This is Slug ${found.id}")
        articleRepository.delete(found)
    }

    @Transactional
    override fun favoriteArticle(slug: String, authUserDetails: AuthUserDetails): ArticleDto {
        val found = articleRepository.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)

        if (favoriteRepository.findByArticleIdAndAuthorEmail(found.id, authUserDetails.email).isPresent) {
            throw AppException(Error.ALREADY_FAVORITED_ARTICLE)
        }

        val favorite = FavoriteDocument.builder()
                .article(found)
                .author(UserDocument.builder()
                        .id(authUserDetails.id.toString())
                        .bio(authUserDetails.bio)
                        .email(authUserDetails.email)
                        .build())
                .build()

        favoriteRepository.save(favorite)

        return getArticle(slug, authUserDetails)
    }


    private fun convertToArticleList(articleEntities: List<ArticleDocument>, authUserDetails: AuthUserDetails): List<ArticleDto> {
        return articleEntities.map { entity ->
            val favorites = entity.favoriteList
            val favorited = favorites.any { favoriteEntity -> favoriteEntity.author.id == authUserDetails.id.toString() }
            val favoriteCount = favorites.size.toLong()
            convertEntityToDto(entity, favorited, favoriteCount, authUserDetails)
        }
    }



    override fun feedArticles(authUserDetails: AuthUserDetails, feedParams: FeedParams): List<ArticleDto> {
        val feedAuthorIds = followRepository.findByFollowerId(authUserDetails.id.toString()).map { it.followee.id }
        return articleRepository.findByAuthorId(feedAuthorIds, PageRequest.of(feedParams.offset, feedParams.limit))
                .map { entity ->
                    val favoriteEntities = favoriteRepository.findAllFavorites()
                    val favorited = favoriteRepository.findByAuthorEmail(authUserDetails.email).isPresent
                    val favoriteCount = favoriteEntities.size.toLong()
                    convertEntityToDto(entity, true, favoriteCount, authUserDetails)
                }
    }



    override fun getAllArticles(authUserDetails: AuthUserDetails): List<ArticleDto> {
        val result = articleRepository.findByAuthorId(authUserDetails.id)
        val articleEntities = articleRepository.findAllArticles()
        val favoriteEntities = favoriteRepository.findAllFavorites()
        val favorited = favoriteRepository.findByAuthorEmail(authUserDetails.email).isPresent
        val favoriteCount = favoriteEntities.size.toLong()

        return result.map { articleEntity ->
            convertEntityToDto(articleEntity, favorited, favoriteCount, authUserDetails)
        }
    }


    override fun getAllArticlesYouFollow(): List<ArticleDto> {
        val articleEntities = articleRepository.findAllArticlesYouFollow()
        val favoriteEntities = favoriteRepository.findAllFavorites()
        val favoriteCount = favoriteEntities.size.toLong()

        return articleEntities.map { articleEntity ->
            convertEntityToDtos(articleEntity, true, favoriteCount)
        }
    }

    private fun convertEntityToDtos(entity: ArticleDocument, favorited: Boolean, favoritesCount: Long): ArticleDto {
        val author = profileService.getProfileByUserIds(entity.author.id)
        return ArticleDto(
                id = entity.id,
                slug = entity.slug,
                title = entity.title,
                description = entity.description,
                body = entity.body,
                author = author,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                favorited = favorited,
                favoritesCount = favoritesCount,
                tagList = entity.tagList
        )
    }



}
