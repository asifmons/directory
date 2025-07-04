package com.stjude.directory.dto;


import com.stjude.directory.enums.BloodGroup;
import com.stjude.directory.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CreateMemberRequest {

    @NotBlank(message = "Member name is required")
    @Size(max = 100, message = "Member name must be less than 100 characters")
    public String name;

    @NotBlank(message = "Date of Birth is required")
    public Date dob;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10,12}$", message = "Phone number must be between 10 and 12 digits")
    public String phoneNumber;

    @NotBlank(message = "Email Id is required.")
    @Email(message = "Email Id is in invalid format.")
    public String emailId;

    public BloodGroup bloodGroup;

    public Boolean isFamilyHead;

    public Short coupleNo;
    public String password;
    public List<Role> roles;
}