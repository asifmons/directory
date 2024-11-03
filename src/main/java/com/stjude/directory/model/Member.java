package com.stjude.directory.model;

import com.stjude.directory.dto.CreateMemberRequest;
import com.stjude.directory.dto.UpdateMemberRequest;
import com.stjude.directory.enums.BloodGroup;
import com.stjude.directory.enums.Role;
import com.stjude.directory.enums.Unit;
import com.stjude.directory.utils.StringOps;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "family_members")
@NoArgsConstructor
public class Member {
    private String id;  // Unique ID for each family member
    private String familyId;
    private String name;
    private String dob;
    private String phoneNumber;
    private String emailId;
    private BloodGroup bloodGroup;
    private Boolean isFamilyHead;
    private String address;
    private Unit unit;
    private Short coupleNo;
    private String password;
    private List<Role> roles;

    public Member(CreateMemberRequest request, String familyId, String address, Unit unit){
        this.id = StringOps.generateUUID();
        this.familyId = familyId;
        this.name = request.getName();
        this.dob = request.getDob();
        this.phoneNumber = request.getPhoneNumber();
        this.emailId = request.getEmailId();
        this.bloodGroup = request.getBloodGroup();
        this.isFamilyHead = request.getIsFamilyHead();
        this.address = address;
        this.unit = unit;
        this.coupleNo = request.getCoupleNo();
        this.password = request.getPassword();
        this.roles = request.getRoles();
    }

    public Member(String id, CreateMemberRequest request, String familyId, String address, Unit unit){
        this.id = id;
        this.familyId = familyId;
        this.name = request.getName();
        this.dob = request.getDob();
        this.phoneNumber = request.getPhoneNumber();
        this.emailId = request.getEmailId();
        this.bloodGroup = request.getBloodGroup();
        this.isFamilyHead = request.getIsFamilyHead();
        this.address = address;
        this.unit = unit;
        this.coupleNo = request.getCoupleNo();
    }

    public Member(UpdateMemberRequest request, String familyId, String address, Unit unit){
        this.id = request.getId();
        this.familyId = familyId;
        this.name = request.getName();
        this.dob = request.getDob();
        this.phoneNumber = request.getPhoneNumber();
        this.emailId = request.getEmailId();
        this.bloodGroup = request.getBloodGroup();
        this.isFamilyHead = request.getIsFamilyHead();
        this.address = address;
        this.unit = unit;
        this.coupleNo = request.getCoupleNo();
    }

}
