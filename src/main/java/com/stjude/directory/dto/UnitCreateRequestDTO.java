package com.stjude.directory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitCreateRequestDTO {
    private String name;
    private Integer numberOfFamilies;
}
