package com.stjude.directory.dto;

import com.stjude.directory.model.Couple;
import com.stjude.directory.model.Family;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FamilyResponseDTO {
    private String id;
    private String address;
    private String anniversaryDate;
    private String photoUrl; // URL of the family photo
    private List<FamilyMemberResponseDTO> familyMembers;
    private List<Couple> couples;

    public FamilyResponseDTO(Family family) {
        this.id = family.getId();
        this.address = family.getAddress();
        this.anniversaryDate = family.getAnniversaryDate();
        this.photoUrl = family.getPhotoUrl();
        this.familyMembers = family.getFamilyMembers()
                .stream()
                .map(FamilyMemberResponseDTO::new)
                .toList();
        this.couples = family.getCouples();
    }
}
