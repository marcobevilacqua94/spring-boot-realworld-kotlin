package com.springboot.couchbase.springbootrealworld.domain.article.model

data class ArticleQueryParam(
        var tag: String?,
        var author: String?,
        var favorited: String?
) : FeedParams()
