package com.stjude.directory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Comment {
    @Id
    private String id;
    private String eventId;
    private String userId;
    private String userName;
    private String comment;
    private LocalDateTime createdAt;

    public Comment(String eventId, String userId, String userName, String comment) {
        this.eventId = eventId;
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }
}
