package com.stjude.directory.dto;

import java.util.Map;
import lombok.Data;

@Data
public class AssignRepresentativesRequestDTO {
    private String unitId;
    private String templateId;
    private Map<String, Map<String, String>> cardPositionMemberMap;
}
