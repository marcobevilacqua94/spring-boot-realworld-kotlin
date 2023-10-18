package com.springboot.couchbase.springbootrealworld.domain.article.service

import com.springboot.couchbase.springbootrealworld.domain.article.dto.ArticleDto
import com.springboot.couchbase.springbootrealworld.domain.article.dto.FavoriteDto
import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument
import com.springboot.couchbase.springbootrealworld.domain.article.entity.FavoriteDocument
import com.springboot.couchbase.springbootrealworld.domain.article.repository.ArticleRepository
import com.springboot.couchbase.springbootrealworld.domain.article.repository.FavoriteRepository
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto
import com.springboot.couchbase.springbootrealworld.domain.profile.service.ProfileService
import com.springboot.couchbase.springbootrealworld.domain.user.repository.UserRepository
import com.springboot.couchbase.springbootrealworld.exception.AppException
import com.springboot.couchbase.springbootrealworld.exception.Error
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class FavoriteServiceImpl @Autowired constructor(
        private val articleRepository: ArticleRepository,
        private val articleService: ArticleService,
        private val favoriteRepository: FavoriteRepository,
        private val profileService: ProfileService,
        private val userRepository: UserRepository
) : FavoriteService {

    override fun getFavoritesBySlug(slug: String, authUserDetails: AuthUserDetails): List<FavoriteDto> {
        val articleId = articleRepository.findBySlug(slug).id ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        val favoriteEntities = favoriteRepository.findByArticleId(articleId)
        return favoriteEntities.map { convertToDTO(authUserDetails, it) }
    }

    //@Transactional
    override fun delete(slug: String, authUserDetails: AuthUserDetails): ArticleDto {
        val article = articleRepository.findBySlug(slug) ?: throw AppException(Error.ARTICLE_NOT_FOUND)
        val articleId = article.id
        val favoriteEntities = favoriteRepository.findByArticleId(articleId!!)
        favoriteRepository.deleteAll(favoriteEntities)
        return articleService.getArticle(slug, authUserDetails)
    }

    private fun convertToDTO(authUserDetails: AuthUserDetails, favoriteDocument: FavoriteDocument): FavoriteDto {
        val author = profileService.getProfileByUserId(authUserDetails)
        return FavoriteDto(
                id = favoriteDocument.id,
                author = author
        )
    }
}
