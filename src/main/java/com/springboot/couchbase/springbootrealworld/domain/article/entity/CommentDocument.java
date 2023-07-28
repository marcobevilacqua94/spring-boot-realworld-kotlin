package com.springboot.couchbase.springbootrealworld.domain.article.entity;


import com.springboot.couchbase.springbootrealworld.domain.common.entiity.BaseDocument;
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document
@ToString
public class CommentDocument extends BaseDocument {

    public final static String COMMENT_COLLECTION_NAME = "comment";

    @Field
    private String body;
    @Field
    private UserDocument author;
    @Field
    private ArticleDocument article;

}

