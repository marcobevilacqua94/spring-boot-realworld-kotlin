package com.springboot.couchbase.springbootrealworld.domain.article.repository;


import com.springboot.couchbase.springbootrealworld.domain.article.entity.CommentDocument;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Collection(CommentDocument.COMMENT_COLLECTION_NAME)
public interface CommentRepository extends CrudRepository<CommentDocument, String> {
    List<CommentDocument> findByArticleIdOrderByCreatedAtDesc(String articleId);
}

