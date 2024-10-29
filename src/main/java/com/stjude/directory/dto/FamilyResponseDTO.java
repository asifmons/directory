package com.stjude.directory.dto;

import com.stjude.directory.model.Couple;
import com.stjude.directory.model.Family;
import com.stjude.directory.model.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FamilyResponseDTO {
    private String id;
    private String address;
    private String photoUrl; // URL of the family photo
    private List<MemberResponseDTO> familyMembers;
    private List<Couple> couples;
    private String houseName;

    public FamilyResponseDTO(Family family, List<Member> members) {
        this.id = family.getId();
        this.address = family.getAddress();
        this.photoUrl = family.getPhotoUrl();
        this.houseName = family.getHouseName();
        this.familyMembers = members
                .stream()
                .map(MemberResponseDTO::new)
                .toList();
        //this.couples = family.getAnniversaryDates();//todo -populate couple- do as part of feature-3
    }
}
