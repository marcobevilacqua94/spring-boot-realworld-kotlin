package com.springboot.couchbase.springbootrealworld.domain.article.entity;

import com.springboot.couchbase.springbootrealworld.domain.common.entiity.BaseDocument;
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
@SuperBuilder
public class ArticleDocument extends BaseDocument {

    public final static String ARTICLE_COLLECTION_NAME = "article";

    @Field
    private String slug;
    @Field
    private String title;
    @Field
    private String description;
    @Field
    private String body;

    @Field
    private List<String> tagList = Collections.EMPTY_LIST;

    private List<FavoriteDocument> favoriteList;

    @Field
    private UserDocument author;

    @Builder
    public ArticleDocument(String slug, String title, String description, String body, List<String> taglist, UserDocument author) {
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = taglist;
        this.author = author;
    }


}

