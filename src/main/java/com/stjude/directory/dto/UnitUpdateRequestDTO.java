package com.stjude.directory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitUpdateRequestDTO {
    private String name;
    private Integer numberOfFamilies;
}
