package com.stjude.directory.model;

import com.stjude.directory.dto.CreateMemberRequest;
import com.stjude.directory.dto.MemberRowCSVTemplate;
import com.stjude.directory.dto.UpdateMemberRequest;
import com.stjude.directory.enums.BloodGroup;
import com.stjude.directory.enums.Role;
import com.stjude.directory.enums.Status;
import com.stjude.directory.enums.Unit;
import com.stjude.directory.utils.StringOps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "MEMBER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private String id;  // Unique ID for each family member
    private String familyId;
    private String name;
    private Date dob;//should be in 23-12
    private String phoneNumber;
    private String emailId;
    private BloodGroup bloodGroup;
    private Boolean isFamilyHead;
    private String address;
    private Unit unit;
    private Short coupleNo;
    private String salutation;//todo - add this in all requests
    private String password;
    private List<Role> roles;
    private Short parentCoupleNo;
    private Status status;
    private Date expiryDate;
    private String houseName;

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

    public Member(MemberRowCSVTemplate template, String familyId){
        this.id = StringOps.generateUUID();
        this.familyId = familyId;
        this.name = template.getMemberName();
        this.dob = template.getDob();
        this.phoneNumber = template.getPhoneNumber();
        this.emailId = template.getEmailId();
        this.bloodGroup = template.getBloodGroup();
        this.isFamilyHead = template.getIsFamilyHead();
        this.address = template.getAddress();
        this.unit = template.getUnit();
        this.coupleNo = template.getCoupleNo();
        this.password = template.getPassword();
        this.salutation = template.getSalutation();
        this.status = template.getStatus();
        this.expiryDate = template.getExpiryDate();
    }

}
