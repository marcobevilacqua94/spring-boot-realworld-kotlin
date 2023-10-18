package com.springboot.couchbase.springbootrealworld.domain.article.service;

import com.springboot.couchbase.springbootrealworld.domain.article.dto.ArticleDto;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.FavoriteDocument;
import com.springboot.couchbase.springbootrealworld.domain.article.model.FeedParams;
import com.springboot.couchbase.springbootrealworld.domain.article.repository.ArticleRepository;
import com.springboot.couchbase.springbootrealworld.domain.article.repository.CommentRepository;
import com.springboot.couchbase.springbootrealworld.domain.article.repository.FavoriteRepository;
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto
import com.springboot.couchbase.springbootrealworld.domain.profile.repository.FollowRepository;
import com.springboot.couchbase.springbootrealworld.domain.profile.service.ProfileService;
import com.springboot.couchbase.springbootrealworld.domain.tag.entity.ArticleTagRelationDocument;
import com.springboot.couchbase.springbootrealworld.domain.tag.repository.TagRepository;
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import com.springboot.couchbase.springbootrealworld.exception.Error;
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails;
import lombok.RequiredArgsConstructor
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
open class ArticleServiceImpl(

) : ArticleService {

    @Autowired private val articleRepository: ArticleRepository?  = null
    @Autowired private val favoriteRepository: FavoriteRepository?  = null
    @Autowired private val profileService: ProfileService?  = null
    @Autowired private val followRepository: FollowRepository?  = null
    @Autowired private val commentRepository: CommentRepository?  = null
    @Autowired private val tagRepository: TagRepository?  = null

    private val logger: Logger = LoggerFactory.getLogger(ArticleServiceImpl::class.java)
    //@Transactional
    override fun createArticle(article: ArticleDto, authUserDetails: AuthUserDetails): ArticleDto {
        val slug = article.title!!.split(" ").joinToString("-")
        val author = UserDocument(id = authUserDetails.id)

        val articleDocument = ArticleDocument(
                slug = slug,
                title = article.title,
                description = article.description,
                body = article.body,
                taglist = article.tagList?.sorted(),
                author = author
        )

        article.tagList?.forEach { tag ->
            tagRepository!!.save(ArticleTagRelationDocument(
                tagvalue = tag,
                article = articleDocument))
        }

        val savedArticle = articleRepository!!.save(articleDocument)

        return convertEntityToDto(savedArticle, false, 0L, authUserDetails)
    }


    override fun getArticleFavorite(slug: String, authUserDetails: AuthUserDetails): ArticleDto {
        val result = articleRepository!!.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        return convertEntityToDto(result, false, 0L, authUserDetails)
    }

    override fun getArticle(slug: String, authUserDetails: AuthUserDetails): ArticleDto {
        val result = articleRepository!!.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        val favoriteEntities = favoriteRepository!!.findAllFavorites()
        val articleId = result.id
        val favorited = articleId?.let {
            favoriteRepository.findByArticleIdAndAuthorEmail(it, authUserDetails.email) != null
        } ?: false
        val favoriteCount = favoriteEntities.size.toLong()

        return convertEntityToDto(result, favorited, favoriteCount, authUserDetails)
    }



    private fun convertEntityToDto(entity: ArticleDocument, favorited: Boolean, favoritesCount: Long, authUserDetails: AuthUserDetails): ArticleDto {
        val articleDto = ArticleDto(
                id = entity.id!!,
                slug = entity.slug,
                title = entity.title,
                description = entity.description,
                body = entity.body,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                favorited = favorited,
                favoritesCount = favoritesCount,
                tagList = entity.tagList
        )

        val author = profileService!!.getProfileByUserIds(entity.author?.id!!)
        articleDto.author = author

        articleDto.tagList = entity.tagList

        return articleDto
    }

    override fun updateArticle(slug: String, article: ArticleDto.Update, authUserDetails: AuthUserDetails): ArticleDto {
        val found = articleRepository!!.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        if (article.title != null) {
            val newSlug = article.title!!.split(" ").joinToString("-")
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


    //@Transactional
    override fun deleteArticle(slug: String, authUserDetails: AuthUserDetails) {
        val found = articleRepository!!.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        logger.info("This is Slug ${found.id}")
        articleRepository.delete(found)
    }

    //@Transactional
    override fun favoriteArticle(slug: String, authUserDetails: AuthUserDetails): ArticleDto {
        val found = articleRepository!!.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)

        favoriteRepository!!.findByArticleIdAndAuthorEmail(found.id!!, authUserDetails.email)
                ?.let {
                    throw AppException(Error.ALREADY_FAVORITED_ARTICLE)
                }

        val favorite = FavoriteDocument(
                article = found,
                author = UserDocument(
                        id = authUserDetails.id,
                        bio = authUserDetails.bio,
                        email = authUserDetails.email
                ),

        )

        favoriteRepository.save(favorite)

        return getArticle(slug, authUserDetails)
    }


    private fun convertToArticleList(articleEntities: List<ArticleDocument>, authUserDetails: AuthUserDetails): List<ArticleDto> {
        return articleEntities.map { entity ->
            val favorites = entity.favoriteList
            val favorited = favorites!!.any { favoriteEntity -> favoriteEntity.author!!.id == authUserDetails.id.toString() }
            val favoriteCount = favorites.size.toLong()
            convertEntityToDto(entity, favorited, favoriteCount, authUserDetails)
        }
    }



    override fun feedArticles(authUserDetails: AuthUserDetails, feedParams: FeedParams): List<ArticleDto> {
        val feedAuthorIds = followRepository!!.findByFollowerId(authUserDetails.id.toString()).map { it.followee!!.id }
        return articleRepository!!.findByAuthorId(feedAuthorIds, PageRequest.of(feedParams.offset!!, feedParams.limit!!))
                .map { entity ->
                    val favoriteEntities = favoriteRepository!!.findAllFavorites()
                    var favorited = false
                    if(favoriteRepository.findByAuthorEmail(authUserDetails.email).isNotEmpty()) { favorited = true }
                    val favoriteCount = favoriteEntities.size.toLong()
                    convertEntityToDto(entity, favorited, favoriteCount, authUserDetails)
                }
    }



    override fun getAllArticles(authUserDetails: AuthUserDetails): List<ArticleDto> {
        val result = articleRepository!!.findByAuthorId(authUserDetails.id)
        val articleEntities = articleRepository.findAllArticles()
        val favoriteEntities = favoriteRepository!!.findAllFavorites()
        var favorited = false
        if(favoriteRepository.findByAuthorEmail(authUserDetails.email).isNotEmpty()) { favorited = true }
        val favoriteCount = favoriteEntities.size.toLong()

        return result.map { articleEntity ->
            convertEntityToDto(articleEntity, favorited, favoriteCount, authUserDetails)
        }
    }


    override fun getAllArticlesYouFollow(): List<ArticleDto> {
        val articleEntities = articleRepository!!.findAllArticlesYouFollow()
        val favoriteEntities = favoriteRepository!!.findAllFavorites()
        val favoriteCount = favoriteEntities.size.toLong()

        return articleEntities.map { articleEntity ->
            convertEntityToDtos(articleEntity, true, favoriteCount)
        }
    }

    private fun convertEntityToDtos(entity: ArticleDocument, favorited: Boolean, favoritesCount: Long): ArticleDto {
        val author = profileService!!.getProfileByUserIds(entity.author!!.id!!)
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
