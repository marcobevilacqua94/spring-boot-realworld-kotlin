package com.springboot.couchbase.springbootrealworld.domain.tag.entity;

import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ArticleTagRelationDocument {

    public final static String TAG_COLLECTION_NAME = "tag";

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    protected String id;

    @Field
    private String tag;

    @Field()
    private ArticleDocument article;

}