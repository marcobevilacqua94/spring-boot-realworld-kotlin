package com.springboot.couchbase.springbootrealworld.domain.article.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.couchbase.springbootrealworld.configuration.CouchbaseConfig;
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
public class ArticleDto {

    protected String id;
    private String slug;
    private String title;
    private String description;
    private String body;

    private List<String> tagList;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
    private Date updatedAt;
    private Boolean favorited;
    private Long favoritesCount;
    private ProfileDto author;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class SingleArticle<T> {
        private T article;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class MultipleArticle {
        private List<ArticleDto> articles;
        private int articlesCount;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class TagList {
        List<String> tags;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Update {
        private String title;
        private String description;
        private String body;
    }


}

