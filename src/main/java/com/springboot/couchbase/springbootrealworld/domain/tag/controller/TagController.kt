package com.springboot.couchbase.springbootrealworld.domain.tag.controller

import com.springboot.couchbase.springbootrealworld.domain.tag.dto.TagDto
import com.springboot.couchbase.springbootrealworld.domain.tag.service.TagService
import com.springboot.couchbase.springbootrealworld.exception.AppException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/tags")
class TagController @Autowired constructor(
        private val tagService: TagService
) {

    @GetMapping
    fun getArticleTags(): TagDto.TagList {
        return try {
            TagDto.TagList(tags = tagService.getAllTagList())
        } catch (aex: AppException) {
            throw ResponseStatusException(aex.error.status.value(), aex.error.message, aex)
        }
    }
}
