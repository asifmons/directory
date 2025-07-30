package com.stjude.directory.service;

import com.stjude.directory.dto.AssignRepresentativesRequestDTO;
import com.stjude.directory.dto.UnitRepresentativesDetailDTO;
import com.stjude.directory.dto.UnitRepresentativesTemplateDTO;
import com.stjude.directory.model.UnitRepresentatives;
import com.stjude.directory.model.UnitRepresentativesTemplate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UnitRepresentativesService {
    UnitRepresentativesTemplate createTemplate(UnitRepresentativesTemplateDTO templateDTO);
    List<UnitRepresentativesTemplate> getAllTemplates();
    UnitRepresentatives assignRepresentatives(AssignRepresentativesRequestDTO requestDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto);
    UnitRepresentativesDetailDTO getRepresentativesByUnit(String unitId);
    UnitRepresentativesTemplate updateTemplate(String templateId, UnitRepresentativesTemplateDTO templateDTO);
    UnitRepresentatives updateAssignedRepresentatives(String unitId, AssignRepresentativesRequestDTO requestDTO, MultipartFile coverPhoto, MultipartFile innerCoverPhoto);
}
