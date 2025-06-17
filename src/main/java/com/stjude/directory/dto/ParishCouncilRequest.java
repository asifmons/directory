package com.stjude.directory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class ParishCouncilRequest {
    @NotNull(message = "Start year is required")
    @Min(value = 1900, message = "Start year must be at least 1900")
    @Max(value = 2100, message = "Start year must be at most 2100")
    private Integer startYear;
    
    @NotNull(message = "End year is required")
    @Min(value = 1900, message = "End year must be at least 1900")
    @Max(value = 2100, message = "End year must be at most 2100")
    private Integer endYear;
    
    private boolean isActive = false;
} 