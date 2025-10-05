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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/committees")
@Tag(name = "Committees")
public class CommitteeController {

    @Autowired
    private CommitteeService committeeService;

    @PostMapping
    public ResponseEntity<Committee> createCommittee(@RequestPart("data") CommitteeDTO committeeDTO,
                                                     @RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto,
                                                     @RequestPart(value = "innerCoverPhoto", required = false) MultipartFile innerCoverPhoto) {
        return ResponseEntity.ok(committeeService.createCommittee(committeeDTO, coverPhoto, innerCoverPhoto));
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
    public ResponseEntity<Committee> updateCommittee(@PathVariable String committeeId,
                                                     @RequestPart("data") CommitteeDTO committeeDTO,
                                                     @RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto,
                                                     @RequestPart(value = "innerCoverPhoto", required = false) MultipartFile innerCoverPhoto) {
        return ResponseEntity.ok(committeeService.updateCommittee(committeeId, committeeDTO, coverPhoto, innerCoverPhoto));
    }

    @DeleteMapping("/{committeeId}")
    public ResponseEntity<Void> deleteCommittee(@PathVariable String committeeId) {
        committeeService.deleteCommittee(committeeId);
        return ResponseEntity.noContent().build();
    }
}
