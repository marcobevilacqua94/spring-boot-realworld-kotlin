package com.springboot.couchbase.springbootrealworld.domain.article.repository

import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.Collection
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Collection(ArticleDocument.ARTICLE_COLLECTION_NAME)
interface ArticleRepository : ReactiveCouchbaseRepository<ArticleDocument, String> {

    fun findBySlug(slug: String): Mono<ArticleDocument>

    fun findByAuthorId(author: String): Flux<ArticleDocument>

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter} ORDER BY createdAt DESC")
    fun findBySlugs(): Mono<ArticleDocument>

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter}")
    fun findByAuthorId(ids: List<String>, pageable: Pageable): Flux<ArticleDocument>

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter} ORDER BY createdAt DESC")
    fun findAllArticles(): Flux<ArticleDocument>

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter} ORDER BY createdAt DESC")
    fun findAllArticlesYouFollow(): Flux<ArticleDocument>

    @Query("#{#n1ql.selectEntity} WHERE title IS NOT NULL AND #{#n1ql.filter}")
    fun findAllTags(): Flux<ArticleDocument>
}
