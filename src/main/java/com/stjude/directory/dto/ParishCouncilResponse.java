package com.stjude.directory.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ParishCouncilResponse {
    private String id;
    private String imageUrl;
    private Integer startYear;
    private Integer endYear;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 