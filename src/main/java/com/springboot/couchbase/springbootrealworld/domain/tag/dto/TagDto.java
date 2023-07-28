package com.springboot.couchbase.springbootrealworld.domain.tag.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class TagDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagList {
        List<String> tags;
    }


}


