package com.stjude.directory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnitResponseDTO {
    private String id;
    private Integer numberOfFamilies;
    private UnitExecutive president;
    private UnitExecutive vicePresident;
    private UnitExecutive secretary;
    private UnitExecutive treasurer;
    private UnitExecutive jointSecretary;
    private UnitExecutive jointTreasurer;
}
