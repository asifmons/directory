package com.stjude.directory.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("committees")
@Data
public class Committee {
    @Id
    private String id;
    private String name;
    private List<CommitteeCard> cards;
    private String coverPhotoUrl;
    private String innerCoverPhotoUrl;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
