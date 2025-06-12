package com.stjude.directory.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.Objects;


@Document(collection = "highlight_images")
@Data
public class HighlightImage {
    @Id
    private String id;

    @Indexed
    private String highlightId;

    private String s3Url;
    private String originalName;
    private LocalDateTime uploadDate;

    public HighlightImage(String highlightId, String s3Url, String originalName) {
        this.highlightId = Objects.requireNonNull(highlightId, "Highlight ID cannot be null");
        this.s3Url = Objects.requireNonNull(s3Url, "S3 URL cannot be null");
        this.originalName = Objects.requireNonNull(originalName, "Original name cannot be null");
        this.uploadDate = LocalDateTime.now();
    }
}
