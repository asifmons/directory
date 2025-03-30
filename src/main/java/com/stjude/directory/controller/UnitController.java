package com.stjude.directory.controller;

import com.stjude.directory.dto.UnitCreateRequestDTO;
import com.stjude.directory.dto.UnitResponseDTO;
import com.stjude.directory.dto.UnitUpdateRequestDTO;
import com.stjude.directory.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unit")
public class UnitController {
    @Autowired
    private UnitService unitService;
    @PostMapping
    public void createUnit(@RequestBody UnitCreateRequestDTO unitCreateRequestDTO) {
        unitService.createUnit(unitCreateRequestDTO);
    }

    // Update Unit
    @PutMapping("/{id}")
    public void updateUnit(@PathVariable String id, @RequestBody UnitUpdateRequestDTO unitUpdateRequestDTO) {
        unitService.updateUnitFields(id, unitUpdateRequestDTO);
    }

    // Get Unit Details
    @GetMapping("/{id}")
    public ResponseEntity<UnitResponseDTO> getUnit(@PathVariable String id) {
        return ResponseEntity.ok(unitService.getUnitById(id));
    }

    // Delete Unit
    @DeleteMapping("/{id}")
    public void deleteUnit(@PathVariable String id) {
        unitService.deleteUnit(id);
    }

    // Get All Units
    @GetMapping
    public ResponseEntity<List<UnitResponseDTO>> getAllUnits() {
        return ResponseEntity.ok(unitService.getAllUnits());
    }

}
