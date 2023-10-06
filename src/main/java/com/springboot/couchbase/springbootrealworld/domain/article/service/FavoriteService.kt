package com.springboot.couchbase.springbootrealworld.domain.article.service

import com.springboot.couchbase.springbootrealworld.domain.article.dto.ArticleDto
import com.springboot.couchbase.springbootrealworld.domain.article.dto.FavoriteDto
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails

interface FavoriteService {

    fun getFavoritesBySlug(slug: String, authUserDetails: AuthUserDetails): List<FavoriteDto>

    fun delete(slug: String, authUserDetails: AuthUserDetails): ArticleDto
}
