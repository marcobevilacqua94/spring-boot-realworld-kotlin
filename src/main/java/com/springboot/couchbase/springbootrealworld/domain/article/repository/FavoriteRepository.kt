package com.springboot.couchbase.springbootrealworld.domain.article.repository

import com.springboot.couchbase.springbootrealworld.domain.article.entity.FavoriteDocument
import org.springframework.data.couchbase.repository.Collection
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
@Collection(FavoriteDocument.FAVORITE_COLLECTION_NAME)
interface FavoriteRepository : CrudRepository<FavoriteDocument, String> {

    @Query("#{#n1ql.selectEntity} WHERE id IS NOT NULL AND #{#n1ql.filter}")
    fun findBySlug(slug: String): List<FavoriteDocument>

    fun findByArticleIdAndAuthorEmail(articleId: String, author: String): FavoriteDocument?

    fun findByAuthorEmail(author: String): FavoriteDocument?

    fun findByArticleId(articleId: String): List<FavoriteDocument>

    @Query("#{#n1ql.selectEntity} WHERE author IS NOT NULL AND #{#n1ql.filter}")
    fun findAllFavorites(): List<FavoriteDocument>
}
