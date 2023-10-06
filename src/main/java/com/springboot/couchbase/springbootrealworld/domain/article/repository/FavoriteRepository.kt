package com.springboot.couchbase.springbootrealworld.domain.article.repository;


import com.springboot.couchbase.springbootrealworld.domain.article.entity.FavoriteDocument;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Collection(FavoriteDocument.FAVORITE_COLLECTION_NAME)
public interface FavoriteRepository extends CrudRepository<FavoriteDocument, String> {
    @Query("#{#n1ql.selectEntity} WHERE id IS NOT NULL AND #{#n1ql.filter}")
    List<FavoriteDocument> findBySlug(String slug);

    Optional<FavoriteDocument> findByArticleIdAndAuthorEmail(String articleId, String author);


    Optional<FavoriteDocument> findByAuthorEmail(String author);

    List<FavoriteDocument> findByArticleId(String articleId);

    @Query("#{#n1ql.selectEntity} WHERE author IS NOT NULL AND #{#n1ql.filter}")
    List<FavoriteDocument> findAllFavorites();


}
