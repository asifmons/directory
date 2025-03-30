package com.stjude.directory.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class UnitUpdateRequestDTO {
    private Integer numberOfFamilies;
    private String president;
    private String vicePresident;
    private String secretary;
    private String treasurer;

    public UnitUpdateRequestDTO(int numberOfFamilies, String president, String vicePresident, String secretary, String treasurer) {
        this.numberOfFamilies = numberOfFamilies;
        this.president = president;
        this.vicePresident = vicePresident;
        this.secretary = secretary;
        this.treasurer = treasurer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitUpdateRequestDTO that = (UnitUpdateRequestDTO) o;
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
