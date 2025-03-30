package com.stjude.directory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "UNIT")
public class Unit {
    @Id
    private String id;
    private int numberOfFamilies;
    private String president;
    private String vicePresident;
    private String secretary;
    private String treasurer;

    public Unit(int numberOfFamilies, String president, String vicePresident, String secretary, String treasurer) {
        this.numberOfFamilies = numberOfFamilies;
        this.president = president;
        this.vicePresident = vicePresident;
        this.secretary = secretary;
        this.treasurer = treasurer;
    }


}
