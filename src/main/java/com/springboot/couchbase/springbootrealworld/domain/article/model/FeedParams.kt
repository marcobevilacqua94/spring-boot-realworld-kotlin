package com.springboot.couchbase.springbootrealworld.domain.article.model

import javax.validation.constraints.AssertTrue

data class FeedParams(
        var offset: Int?,
        var limit: Int?
) {
    @AssertTrue
    fun isValidPage(): Boolean {
        return (offset != null && limit != null) || (offset == null && limit == null)
    }
}
