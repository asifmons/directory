package com.stjude.directory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "UNIT")
public class Unit {
    @Id
    private String id;
    private Integer numberOfFamilies;
    private String president;
    private String vicePresident;
    private String secretary;
    private String treasurer;
    private String jointSecretary;
    private String jointTreasurer;




}
