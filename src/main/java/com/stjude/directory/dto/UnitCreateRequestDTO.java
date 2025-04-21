package com.stjude.directory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitCreateRequestDTO {
    private Integer numberOfFamilies;
    private String president;
    private String vicePresident;
    private String secretary;
    private String treasurer;
    private String jointSecretary;
    private String jointTreasurer;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitCreateRequestDTO that = (UnitCreateRequestDTO) o;
        return numberOfFamilies == that.numberOfFamilies && Objects.equals(president, that.president) && Objects.equals(vicePresident, that.vicePresident) && Objects.equals(secretary, that.secretary) && Objects.equals(treasurer, that.treasurer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfFamilies, president, vicePresident, secretary, treasurer);
    }

    @Override
    public String toString() {
        return "UnitCreateRequestDTO{" +
                "numberOfFamilies=" + numberOfFamilies +
                ", president='" + president + '\'' +
                ", vicePresident='" + vicePresident + '\'' +
                ", secretary='" + secretary + '\'' +
                ", treasurer='" + treasurer + '\'' +
                '}';
    }
}
