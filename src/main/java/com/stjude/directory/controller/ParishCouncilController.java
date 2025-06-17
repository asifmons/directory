package com.stjude.directory.controller;

import com.stjude.directory.dto.ParishCouncilRequest;
import com.stjude.directory.dto.ParishCouncilResponse;
import com.stjude.directory.service.ParishCouncilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parish-councils")
public class ParishCouncilController {

    @Autowired
    private ParishCouncilService parishCouncilService;

    // Add a new parish council
    @PostMapping
    public ResponseEntity<ParishCouncilResponse> addParishCouncil(@Valid @RequestBody ParishCouncilRequest request) {
        try {
            ParishCouncilResponse savedCouncil = parishCouncilService.addParishCouncil(request);
            return new ResponseEntity<>(savedCouncil, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all parish councils
    @GetMapping
    public ResponseEntity<List<ParishCouncilResponse>> getAllParishCouncils() {
        try {
            List<ParishCouncilResponse> councils = parishCouncilService.getAllParishCouncils();
            return new ResponseEntity<>(councils, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get current parish council
    @GetMapping("/current")
    public ResponseEntity<ParishCouncilResponse> getCurrentParishCouncil() {
        try {
            Optional<ParishCouncilResponse> currentCouncil = parishCouncilService.getCurrentParishCouncil();
            if (currentCouncil.isPresent()) {
                return new ResponseEntity<>(currentCouncil.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get parish council by ID
    @GetMapping("/{id}")
    public ResponseEntity<ParishCouncilResponse> getParishCouncilById(@PathVariable String id) {
        try {
            Optional<ParishCouncilResponse> council = parishCouncilService.getParishCouncilById(id);
            if (council.isPresent()) {
                return new ResponseEntity<>(council.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get parish councils by year
    @GetMapping("/year/{year}")
    public ResponseEntity<List<ParishCouncilResponse>> getParishCouncilsByYear(@PathVariable Integer year) {
        try {
            List<ParishCouncilResponse> councils = parishCouncilService.getParishCouncilsByYear(year);
            return new ResponseEntity<>(councils, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update parish council
    @PutMapping("/{id}")
    public ResponseEntity<ParishCouncilResponse> updateParishCouncil(
            @PathVariable String id, 
            @Valid @RequestBody ParishCouncilRequest request) {
        try {
            Optional<ParishCouncilResponse> updatedCouncil = parishCouncilService.updateParishCouncil(id, request);
            if (updatedCouncil.isPresent()) {
                return new ResponseEntity<>(updatedCouncil.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete parish council
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteParishCouncil(@PathVariable String id) {
        try {
            boolean deleted = parishCouncilService.deleteParishCouncil(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Upload parish council image
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ParishCouncilResponse> uploadParishCouncilImage(
            @PathVariable String id,
            @RequestParam("image") MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Optional<ParishCouncilResponse> updatedCouncil = parishCouncilService.uploadParishCouncilImage(id, imageFile);
            if (updatedCouncil.isPresent()) {
                return new ResponseEntity<>(updatedCouncil.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 