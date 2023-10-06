package com.springboot.couchbase.springbootrealworld.domain.article.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.couchbase.springbootrealworld.configuration.CouchbaseConfig;
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class CommentDto {

    protected String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CouchbaseConfig.ISO_8601_PATTERN)
    private Date updatedAt;
    private String body;
    private ProfileDto author;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class SingleComment {
        CommentDto comment;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class MultipleComments {
        List<CommentDto> comments;
    }
}
