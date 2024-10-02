package com.stjude.directory.model;

import com.stjude.directory.enums.BloodGroup;
import lombok.Data;

@Data
public class FamilyMember {
    private String id;  // Unique ID for each family member
    private String name;
    private String dob;
    private String phoneNumber;
    private BloodGroup bloodGroup;

}
