package com.stjude.directory.dto;

import com.stjude.directory.enums.BloodGroup;
import lombok.Data;

@Data
public class FamilyMemberResponseDTO {

    private String id;
    private String name;
    private String dob;
    private String phoneNumber;
    private BloodGroup bloodGroup;

}
