package com.stjude.directory.dto;

import com.stjude.directory.enums.Unit;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class CreateFamilyRequest {

    @Nullable
    @Size(max = 200, message = "Address must be less than 200 characters")
    private String address;

    private Unit unit;

    private String houseName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Map<Short, Date> anniversaryDates;

    private List<CreateMemberRequest> familyMembers;

}

