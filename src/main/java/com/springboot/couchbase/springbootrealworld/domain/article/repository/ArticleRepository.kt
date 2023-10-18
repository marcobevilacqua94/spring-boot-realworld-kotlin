package com.springboot.couchbase.springbootrealworld.domain.article.repository

import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.Collection
import org.springframework.data.couchbase.repository.CouchbaseRepository
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
@Collection(ArticleDocument.ARTICLE_COLLECTION_NAME)
interface ArticleRepository : CouchbaseRepository<ArticleDocument, String> {

    fun findBySlug(slug: String): ArticleDocument

    fun findByAuthorId(author: String): List<ArticleDocument>

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter} ORDER BY createdAt DESC")
    fun findBySlugs(): ArticleDocument

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter}")
    fun findByAuthorId(ids: List<String?>, pageable: Pageable): List<ArticleDocument>

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter} ORDER BY createdAt DESC")
    fun findAllArticles(): List<ArticleDocument>

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter} ORDER BY createdAt DESC")
    fun findAllArticlesYouFollow(): List<ArticleDocument>

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter}")
    fun findAllTags(): List<ArticleDocument>
}
