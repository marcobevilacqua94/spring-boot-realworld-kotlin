package com.springboot.couchbase.springbootrealworld.domain.article.repository
import org.springframework.data.couchbase.repository.Collection
import org.springframework.data.couchbase.repository.Query;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.CommentDocument
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
@Collection(CommentDocument.COMMENT_COLLECTION_NAME)
interface CommentRepository : CrudRepository<CommentDocument, String> {

    @Query("#{#n1ql.selectEntity} WHERE article.id = $1 ORDER BY createdAt DESC")
    fun findByArticleIdOrderByCreatedAtDesc(articleId: String): List<CommentDocument>
}
