package com.springboot.couchbase.springbootrealworld.domain.tag.repository;

import com.springboot.couchbase.springbootrealworld.domain.tag.entity.ArticleTagRelationDocument;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Collection(ArticleTagRelationDocument.TAG_COLLECTION_NAME)
public interface TagRepository extends CrudRepository<ArticleTagRelationDocument, String> {

}
