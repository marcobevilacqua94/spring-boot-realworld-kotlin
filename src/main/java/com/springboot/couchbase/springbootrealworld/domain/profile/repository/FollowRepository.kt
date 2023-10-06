package com.springboot.couchbase.springbootrealworld.domain.profile.repository;

import com.springboot.couchbase.springbootrealworld.domain.profile.entity.FollowDocument;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Collection(FollowDocument.FOLLOW_COLLECTION_NANE)
public interface FollowRepository extends CrudRepository<FollowDocument, String> {
    Optional<FollowDocument> findByFolloweeIdAndFollowerId(String followeeId, String followerId);

    List<FollowDocument> findByFollowerId(String id);

}
