package com.springboot.couchbase.springbootrealworld.domain.article.service

import com.springboot.couchbase.springbootrealworld.domain.article.dto.ArticleDto
import com.springboot.couchbase.springbootrealworld.domain.article.model.FeedParams
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails

interface ArticleService {

    fun createArticle(article: ArticleDto, authUserDetails: AuthUserDetails): ArticleDto

    fun getArticle(slug: String, authUserDetails: AuthUserDetails): ArticleDto

    fun getArticleFavorite(slug: String, authUserDetails: AuthUserDetails): ArticleDto

    fun updateArticle(slug: String, article: ArticleDto.Update, authUserDetails: AuthUserDetails): ArticleDto

    fun deleteArticle(slug: String, authUserDetails: AuthUserDetails)

    fun favoriteArticle(slug: String, authUserDetails: AuthUserDetails): ArticleDto

    fun feedArticles(authUserDetails: AuthUserDetails, feedParams: FeedParams): List<ArticleDto>

    fun getAllArticles(authUserDetails: AuthUserDetails): List<ArticleDto>

    fun getAllArticlesYouFollow(): List<ArticleDto>
}
