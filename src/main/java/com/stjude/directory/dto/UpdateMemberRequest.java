package com.stjude.directory.dto;


import com.stjude.directory.enums.BloodGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateMemberRequest {

    @NotBlank(message = "Member Id is required")
    private String id;

    @NotBlank(message = "Member name is required")
    @Size(max = 100, message = "Member name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Date of Birth is required")
    private Date dob;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10,12}$", message = "Phone number must be between 10 and 12 digits")
    private String phoneNumber;

    @NotBlank(message = "Email Id is required.")
    @Email(message = "Email Id is in invalid format.")
    private String emailId;

    private BloodGroup bloodGroup;

    private Boolean isFamilyHead;

    private Short coupleNo;

}