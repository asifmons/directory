package com.stjude.directory.dto;

import com.stjude.directory.enums.BloodGroup;
import com.stjude.directory.enums.Unit;
import com.stjude.directory.model.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberResponseDTO {

    private String id;
    private String familyId;
    private String name;
    private String dob;
    private String phoneNumber;
    private BloodGroup bloodGroup;
    private Boolean isFamilyHead;
    private String emailId;
    private Unit unit;

    public MemberResponseDTO(Member member) {
        this.setId(member.getId());
        this.setFamilyId(member.getFamilyId());
        this.setName(member.getName());
        this.setDob(member.getDob());
        this.setPhoneNumber(member.getPhoneNumber());
        this.setBloodGroup(member.getBloodGroup());
        this.setIsFamilyHead(member.getIsFamilyHead());
        this.setEmailId(member.getEmailId());
        this.setUnit(member.getUnit());
    }
}
