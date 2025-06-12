package com.stjude.directory.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HighlightPreviewResponse {
    private final String highlightId;
    private final String name;
    private final LocalDateTime createdDate;
    private final int totalImages;
    private final List<String> previewImages;

    public HighlightPreviewResponse(String highlightId, String name, LocalDateTime createdDate,
                                    int totalImages, List<String> previewImages) {
        this.highlightId = highlightId;
        this.name = name;
        this.createdDate = createdDate;
        this.totalImages = totalImages;
        this.previewImages = previewImages;
    }
}