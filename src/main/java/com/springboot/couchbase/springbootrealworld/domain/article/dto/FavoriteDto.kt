package com.springboot.couchbase.springbootrealworld.domain.article.dto

import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto

data class FavoriteDto(
        var id: String?,
        var body: String?,
        var author: ProfileDto?
) {
    data class SingleFavorite(
            var favorite: FavoriteDto?
    )

    data class MultipleFavorites(
            var favorites: List<FavoriteDto>?
    )
}