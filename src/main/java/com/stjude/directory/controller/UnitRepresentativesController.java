package com.stjude.directory.controller;

import com.stjude.directory.dto.AssignRepresentativesRequestDTO;
import com.stjude.directory.dto.UnitRepresentativesDetailDTO;
import com.stjude.directory.dto.UnitRepresentativesTemplateDTO;
import com.stjude.directory.model.UnitRepresentatives;
import com.stjude.directory.model.UnitRepresentativesTemplate;
import com.stjude.directory.service.UnitRepresentativesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/unit-representatives")
@Tag(name = "Unit Representatives")
public class UnitRepresentativesController {

    @Autowired
    private UnitRepresentativesService unitRepresentativesService;

    @PostMapping("/templates")
    public ResponseEntity<UnitRepresentativesTemplate> createTemplate(@RequestBody UnitRepresentativesTemplateDTO templateDTO) {
        return ResponseEntity.ok(unitRepresentativesService.createTemplate(templateDTO));
    }

    @GetMapping("/templates")
    public ResponseEntity<List<UnitRepresentativesTemplate>> getAllTemplates() {
        return ResponseEntity.ok(unitRepresentativesService.getAllTemplates());
    }

    @PutMapping("/templates/{templateId}")
    public ResponseEntity<UnitRepresentativesTemplate> updateTemplate(@PathVariable String templateId, @RequestBody UnitRepresentativesTemplateDTO templateDTO) {
        return ResponseEntity.ok(unitRepresentativesService.updateTemplate(templateId, templateDTO));
    }

    @PostMapping("/assign")
    public ResponseEntity<UnitRepresentatives> assignRepresentatives(@RequestPart("data") AssignRepresentativesRequestDTO requestDTO,
                                                                     @RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto,
                                                                     @RequestPart(value = "innerCoverPhoto", required = false) MultipartFile innerCoverPhoto) {
        return ResponseEntity.ok(unitRepresentativesService.assignRepresentatives(requestDTO, coverPhoto, innerCoverPhoto));
    }

    @PutMapping("/assign/{unitId}")
    public ResponseEntity<UnitRepresentatives> updateAssignedRepresentatives(@PathVariable String unitId,
                                                                             @RequestPart("data") AssignRepresentativesRequestDTO requestDTO,
                                                                             @RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto,
                                                                             @RequestPart(value = "innerCoverPhoto", required = false) MultipartFile innerCoverPhoto) {
        return ResponseEntity.ok(unitRepresentativesService.updateAssignedRepresentatives(unitId, requestDTO, coverPhoto, innerCoverPhoto));
    }

    @GetMapping("/{unitId}")
    public ResponseEntity<UnitRepresentativesDetailDTO> getRepresentativesByUnit(@PathVariable String unitId) {
        return ResponseEntity.ok(unitRepresentativesService.getRepresentativesByUnit(unitId));
    }
}
