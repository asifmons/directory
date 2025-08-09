package com.stjude.directory.dto;

import com.stjude.directory.enums.BloodGroup;
import com.stjude.directory.enums.Role;
import com.stjude.directory.enums.Status;
import com.stjude.directory.enums.Unit;
import com.stjude.directory.model.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private List<Role> roles;
    private Status status;
    private String ordinationDate; // For parish priest only
    private String feastDate;
    private String phoneNumberSecondary;

    public MemberResponseDTO(Member member) {
        this.setId(member.getId());
        this.setFamilyId(member.getFamilyId());
        this.setName(member.getName());
        this.setDob(member.getDob() != null ? new SimpleDateFormat("dd-MM-yyyy").format(member.getDob())
                : null);
        this.setPhoneNumber(member.getPhoneNumber());
        this.setBloodGroup(member.getBloodGroup());
        this.setIsFamilyHead(member.getIsFamilyHead());
        this.setEmailId(member.getEmailId());
        this.setUnit(member.getUnit());
        this.setRoles(member.getRoles());
        this.setStatus(member.getStatus() != null ? member.getStatus() : Status.ACTIVE);
        this.setOrdinationDate(member.getOrdinationDate() != null ? new SimpleDateFormat("dd-MM-yy").format( member.getOrdinationDate()) : null);
        this.setFeastDate(member.getOrdinationDate() != null ? new SimpleDateFormat("dd-MM-yy").format(member.getFeastDate()) : null);
        this.setPhoneNumberSecondary(member.getPhoneNumberSecondary());
    }
}
