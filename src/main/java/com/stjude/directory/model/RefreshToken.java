package com.stjude.directory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "refresh_tokens")
public class RefreshToken {
    @Id
    private String id;
    private String emailId;
    @Indexed(name = "expiryTime", expireAfterSeconds = 0) // TTL index to delete the document after expiryDate
    private Instant expiryTime;
}
