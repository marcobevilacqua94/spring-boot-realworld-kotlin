package com.springboot.couchbase.springbootrealworld.domain.tag.entity

import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument
import org.springframework.data.annotation.Id
import org.springframework.data.couchbase.core.mapping.Document
import org.springframework.data.couchbase.core.mapping.Field
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy

@Document
data class ArticleTagRelationDocument(
        @Id
        @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
        val id: String?,

        @Field
        val tag: String?,

        @Field
        val article: ArticleDocument?
)
