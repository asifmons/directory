package com.stjude.directory.dto;


import com.stjude.directory.enums.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class UpdateFamilyRequest {

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must be less than 200 characters")
    private String address;

    private MultipartFile photo; // For uploading family photo

    private List<UpdateMemberRequest> familyMembers;

    private Map<Short, Date> anniversaryDates;

    private Unit unit;

    private String houseName;

}

