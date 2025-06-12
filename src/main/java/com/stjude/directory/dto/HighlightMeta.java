package com.stjude.directory.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Document(collection = "highlight_meta")
@Data
public class HighlightMeta {
    @Id
    private String id;

    @Indexed(unique = true)
    private String highlightId;

    private String name;
    private LocalDateTime createdDate;
    private int totalImages;
    private List<String> previewImageUrls;

    protected HighlightMeta() {
        // For MongoDB
    }

    public HighlightMeta(String highlightId, String name) {
        this.highlightId = Objects.requireNonNull(highlightId, "Highlight ID cannot be null");
        this.name = validateName(name);
        this.createdDate = LocalDateTime.now();
        this.totalImages = 0;
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Name cannot exceed 100 characters");
        }
        return name.trim();
    }

    public void updateImageCounts(int totalImages, List<String> previewUrls) {
        this.totalImages = totalImages;
        this.previewImageUrls = previewUrls;
    }
}