package com.stjude.directory.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
public class FamilyRequestDTO {

    @NotBlank(message = "Family name is required")
    @Size(max = 100, message = "Family name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must be less than 200 characters")
    private String address;

    @NotBlank(message = "Anniversary date is required")
    private String anniversaryDate; // Consider using LocalDate with proper formatting

    private MultipartFile photo; // For uploading family photo

    private List<FamilyMemberRequestDTO> familyMembers;

}

