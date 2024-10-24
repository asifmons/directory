package com.stjude.directory.dto;


import com.stjude.directory.enums.BloodGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMemberRequest {

    @NotBlank(message = "Member name is required")
    @Size(max = 100, message = "Member name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Date of Birth is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "DOB must be in YYYY-MM-DD format")
    private String dob; // Consider using LocalDate with proper formatting

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10,12}$", message = "Phone number must be between 10 and 12 digits")
    private String phoneNumber;

    @Email
    private String emailId;//todo add further validations

    private BloodGroup bloodGroup;

    private Boolean isFamilyHead;
}