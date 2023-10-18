package com.springboot.couchbase.springbootrealworld.domain.profile.repository

import com.springboot.couchbase.springbootrealworld.domain.profile.entity.FollowDocument
import org.springframework.data.couchbase.repository.Collection
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
@Collection(FollowDocument.FOLLOW_COLLECTION_NAME)
interface FollowRepository : CrudRepository<FollowDocument, String> {
    fun findByFolloweeIdAndFollowerId(followeeId: String, followerId: String): Optional<FollowDocument>
    fun findByFollowerId(id: String): List<FollowDocument>
}
