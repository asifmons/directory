package com.stjude.directory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
@Data
@Document(collection = "families")
public class Family {

    @Id
    private String id;
    private String name;
    private String address;
    private String anniversaryDate; // Consider using LocalDate for date handling
    private List<FamilyMember> familyMembers;
    private String photoUrl;

    // Getters and Setters
}


