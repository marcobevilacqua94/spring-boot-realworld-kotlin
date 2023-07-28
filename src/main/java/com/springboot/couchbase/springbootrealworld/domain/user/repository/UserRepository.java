package com.springboot.couchbase.springbootrealworld.domain.user.repository;

import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Collection(UserDocument.USER_COLLECTION_NAME)
public interface UserRepository extends CrudRepository<UserDocument, String> {

    List<UserDocument> findByUsernameOrEmail(String username, String email);

    Optional<UserDocument> findByEmail(String email);

    Optional<UserDocument> findById(String id);

    Optional<UserDocument> findByUsername(String username);


}

