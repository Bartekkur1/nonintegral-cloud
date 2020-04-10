package com.nonintegral.cloud.identity.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class BlacklistToken {

    @Id
    String id;
    String token;

    public BlacklistToken(String token) {
        this.token = token;
    }
}
