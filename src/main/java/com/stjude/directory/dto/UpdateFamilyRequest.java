package com.stjude.directory.dto;

import com.stjude.directory.enums.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class UpdateFamilyRequest {

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must be less than 200 characters")
    private String address;

    private List<UpdateMemberRequest> familyMembers;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Map<Short, Date> anniversaryDates;

    private Unit unit;

    private String houseName;
    private List<AddMemberRequest> familyMembersToAdd;
    private List<String> familyMembersToRemove;
    private List<CoupleRelationshipRequest> coupleRelationships;

}
