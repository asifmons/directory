package com.stjude.directory.model;

import com.stjude.directory.dto.CreateMemberRequest;
import com.stjude.directory.enums.BloodGroup;
import com.stjude.directory.utils.StringOps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class FamilyMember {
    private String id;  // Unique ID for each family member
    private String name;
    private String dob;
    private String phoneNumber;
    private String emailId;
    private BloodGroup bloodGroup;
    private Boolean isFamilyHead;
    private String address;
    private String unit;

    public FamilyMember(CreateMemberRequest request, String address, String unit){
        this.id = StringOps.generateUUID();
        this.name = request.getName();
        this.dob = request.getDob();
        this.phoneNumber = request.getPhoneNumber();
        this.emailId = request.getEmailId();
        this.bloodGroup = request.getBloodGroup();
        this.isFamilyHead = request.getIsFamilyHead();
        this.address = address;
        this.unit = unit;
        //this.anniversaryDate = request.getAnniversaryDate();

    }

}
