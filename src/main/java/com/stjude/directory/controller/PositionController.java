package com.stjude.directory.controller;

import com.stjude.directory.dto.PositionDTO;
import com.stjude.directory.model.Position;
import com.stjude.directory.service.PositionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/positions")
@Tag(name = "Positions")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @PostMapping
    public ResponseEntity<Position> createPosition(@RequestBody PositionDTO positionDTO) {
        return ResponseEntity.ok(positionService.createPosition(positionDTO));
    }

    @GetMapping
    public ResponseEntity<List<Position>> getAllPositions() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(@PathVariable String id, @RequestBody PositionDTO positionDTO) {
        return ResponseEntity.ok(positionService.updatePosition(id, positionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable String id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
