package com.springboot.couchbase.springbootrealworld.domain.user.entity;


import com.springboot.couchbase.springbootrealworld.domain.common.entiity.BaseDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@Document
@SuperBuilder
public class UserDocument extends BaseDocument {

    public final static String USER_COLLECTION_NAME = "user";

    @Field
    private String username;
    @Field
    private String email;
    @Field
    private String password;
    @Field
    private String bio;
    @Field
    private String image;

    @Builder
    public UserDocument(String username, String email, String password, String bio, String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.image = image;
    }
}
