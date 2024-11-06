package com.stjude.directory.dto;


import com.stjude.directory.enums.BloodGroup;
import com.stjude.directory.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateMemberRequest {

    @NotBlank(message = "Member name is required")
    @Size(max = 100, message = "Member name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Date of Birth is required")
    @Pattern(regexp = "^(0\\d|1[0-2])-(0\\d|[1-2]\\d|3[0-1])$", message = "Date of Birth must be in MM-dd format")
    private String dob;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10,12}$", message = "Phone number must be between 10 and 12 digits")
    private String phoneNumber;

    @NotBlank(message = "Email Id is required.")
    @Email(message = "Email Id is in invalid format.")
    private String emailId;

    private BloodGroup bloodGroup;

    private Boolean isFamilyHead;

    private Short coupleNo;
    private String password;
    private List<Role> roles;
}