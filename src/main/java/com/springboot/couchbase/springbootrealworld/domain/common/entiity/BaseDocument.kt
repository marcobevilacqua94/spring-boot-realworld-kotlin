package com.springboot.couchbase.springbootrealworld.domain.common.entiity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.couchbase.core.mapping.Field
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy
import org.springframework.data.couchbase.core.mapping.id.IdAttribute
import java.util.Date

open class BaseDocument(
        @Id
        @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
        @IdAttribute
        open var id: String? = null,

        @Field
        open var createdAt: Date = Date(),

        @Field
        open var updatedAt: Date = Date(),

        @Field
        @Version
        private var version: Long = 0
)
