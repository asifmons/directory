package com.stjude.directory.controller;

import com.stjude.directory.dto.CommitteeDTO;
import com.stjude.directory.dto.CommitteeDetailDTO;
import com.stjude.directory.model.Committee;
import com.stjude.directory.service.CommitteeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/committees")
@Tag(name = "Committees")
public class CommitteeController {

    @Autowired
    private CommitteeService committeeService;

    @PostMapping
    public ResponseEntity<Committee> createCommittee(@RequestBody CommitteeDTO committeeDTO) {
        return ResponseEntity.ok(committeeService.createCommittee(committeeDTO));
    }

    @GetMapping
    public ResponseEntity<List<Committee>> getAllCommittees() {
        return ResponseEntity.ok(committeeService.getAllCommittees());
    }

    @GetMapping("/{committeeId}")
    public ResponseEntity<CommitteeDetailDTO> getCommitteeById(@PathVariable String committeeId) {
        return ResponseEntity.ok(committeeService.getCommitteeById(committeeId));
    }

    @PutMapping("/{committeeId}")
    public ResponseEntity<Committee> updateCommittee(@PathVariable String committeeId, @RequestBody CommitteeDTO committeeDTO) {
        return ResponseEntity.ok(committeeService.updateCommittee(committeeId, committeeDTO));
    }

    @DeleteMapping("/{committeeId}")
    public ResponseEntity<Void> deleteCommittee(@PathVariable String committeeId) {
        committeeService.deleteCommittee(committeeId);
        return ResponseEntity.noContent().build();
    }
}
