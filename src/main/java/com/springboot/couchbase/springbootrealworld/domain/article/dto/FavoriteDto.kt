package com.springboot.couchbase.springbootrealworld.domain.article.dto

import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto

data class FavoriteDto(
        var id: String?,
        var body: String? = null,
        var author: ProfileDto?
) {
    class SingleFavorite(
            var favorite: FavoriteDto?
    )

    class MultipleFavorites(
            var favorites: List<FavoriteDto>?
    )
}