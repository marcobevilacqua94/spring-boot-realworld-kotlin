package com.springboot.couchbase.springbootrealworld.exception

import org.springframework.http.HttpStatus

enum class Error(val message: String, val status: HttpStatus) {
    DUPLICATED_USER("there is duplicated user information", HttpStatus.UNPROCESSABLE_ENTITY),
    LOGIN_INFO_INVALID("login information is invalid", HttpStatus.UNPROCESSABLE_ENTITY),
    ALREADY_FOLLOWED_USER("already followed user", HttpStatus.UNPROCESSABLE_ENTITY),
    ALREADY_FAVORITED_ARTICLE("already followed user", HttpStatus.UNPROCESSABLE_ENTITY),

    USER_NOT_FOUND("user not found", HttpStatus.NOT_FOUND),
    FOLLOW_NOT_FOUND("such follow not found", HttpStatus.NOT_FOUND),
    ARTICLE_NOT_FOUND("article not found", HttpStatus.NOT_FOUND),
    FAVORITE_NOT_FOUND("favorite not found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("comment not found", HttpStatus.NOT_FOUND)
}
