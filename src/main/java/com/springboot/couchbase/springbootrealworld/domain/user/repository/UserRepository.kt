package com.springboot.couchbase.springbootrealworld.domain.user.repository

import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument
import org.springframework.data.couchbase.repository.Collection
import org.springframework.stereotype.Repository

@Repository
@Collection(UserDocument.USER_COLLECTION_NAME)
internal interface UserRepository 