package com.springboot.couchbase.springbootrealworld.domain.profile.entity;

import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
@ToString
public class FollowDocument  {

    public static final String FOLLOW_COLLECTION_NANE = "follow";

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    protected String id;

    @Field
    private UserDocument followee;

    @Field
    private UserDocument follower;

    @Field
    @Version
    private long version;
}

