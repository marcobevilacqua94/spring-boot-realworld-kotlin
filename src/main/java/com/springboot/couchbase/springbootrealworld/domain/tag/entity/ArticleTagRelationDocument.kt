package com.springboot.couchbase.springbootrealworld.domain.tag.entity

import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument
import com.springboot.couchbase.springbootrealworld.domain.common.entiity.BaseDocument
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.couchbase.core.mapping.Document
import org.springframework.data.couchbase.core.mapping.Field
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy
import org.springframework.data.couchbase.core.mapping.id.IdAttribute

@NoArgsConstructor
@AllArgsConstructor
@Document
data class ArticleTagRelationDocument(
        @Id
        @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
        @IdAttribute
        var id: String? = null,
        @Field()
        var article: ArticleDocument? = null,
        @Field
        var tagvalue: String? = null

) {
    companion object {
        const val TAG_COLLECTION_NAME: String = "tag"
    }
}