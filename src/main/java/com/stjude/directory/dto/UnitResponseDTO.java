package com.stjude.directory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnitResponseDTO {
    private String id;
    private Integer numberOfFamilies;
    private String president;
    private String vicePresident;
    private String secretary;
    private String treasurer;
}
