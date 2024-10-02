package com.stjude.directory.dto;

import lombok.Data;

import java.util.List;
@Data
public class FamilyResponseDTO {
    private String id;
    private String name;
    private String address;
    private String anniversaryDate;
    private String photoUrl; // URL of the family photo
    private List<FamilyMemberResponseDTO> familyMembers;
}
