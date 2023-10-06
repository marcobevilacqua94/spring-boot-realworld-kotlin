package com.springboot.couchbase.springbootrealworld.domain.tag.controller;

import com.springboot.couchbase.springbootrealworld.domain.tag.dto.TagDto;
import com.springboot.couchbase.springbootrealworld.domain.tag.service.TagService;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.springboot.couchbase.springbootrealworld.domain.tag.dto.TagDto.TagList;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public TagDto.TagList getArticleTags() {
        try {
            return TagList.builder()
                    .tags(tagService.getAllTagList())
                    .build();
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }
}
