package com.springboot.couchbase.springbootrealworld.domain.tag.dto

data class TagDto(val tags: List<String>? = null) {
    data class TagList(val tags: List<String>? = null)
}
