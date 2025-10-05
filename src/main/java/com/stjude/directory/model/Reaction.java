package com.stjude.directory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Reaction {
    @Id
    private String id;
    private String eventId;
    private String userId;
    private String emoji;

    public Reaction(String eventId, String userId, String emoji) {
        this.eventId = eventId;
        this.userId = userId;
        this.emoji = emoji;
    }
}
