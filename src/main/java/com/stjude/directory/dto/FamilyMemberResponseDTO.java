package com.stjude.directory.dto;

import com.stjude.directory.enums.BloodGroup;
import com.stjude.directory.model.FamilyMember;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FamilyMemberResponseDTO {

    private String id;
    private String name;
    private String dob;
    private String phoneNumber;
    private BloodGroup bloodGroup;
    private Boolean isFamilyHead;
    private String emailId;

    public FamilyMemberResponseDTO(FamilyMember member){
        this.setId(member.getId());
        this.setName(member.getName());
        this.setDob(member.getDob());
        this.setPhoneNumber(member.getPhoneNumber());
        this.setBloodGroup(member.getBloodGroup());
        this.setIsFamilyHead(member.getIsFamilyHead());
        this.setEmailId(member.getEmailId());
    }
}
