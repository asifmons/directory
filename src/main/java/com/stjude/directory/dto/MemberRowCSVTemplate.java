package com.stjude.directory.dto;

import com.stjude.directory.enums.BloodGroup;
import com.stjude.directory.enums.Status;
import com.stjude.directory.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRowCSVTemplate {

    private String familyId;
    private String address;
    private String houseName;
    private String memberName;
    private String relation;
    private Date dob;
    private String phoneNumber;
    private BloodGroup bloodGroup;
    private Boolean isFamilyHead;
    private String emailId;
    private Unit unit;
    private Date anniversaryDate;
    private String salutation;
    private Short coupleNo;
    private String password;
    private Status status;
    private Date expiryDate;
}
