package com.stjude.directory.controller;

import com.stjude.directory.model.Vicar;
import com.stjude.directory.service.VicarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vicars")
public class VicarController {

    @Autowired
    private VicarService vicarService;

    // Add a new vicar
    @PostMapping
    public ResponseEntity<Vicar> addVicar(@RequestBody Vicar vicar) {
        try {
            Vicar savedVicar = vicarService.addVicar(vicar);
            return new ResponseEntity<>(savedVicar, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all vicars
    @GetMapping
    public ResponseEntity<List<Vicar>> getAllVicars() {
        try {
            List<Vicar> vicars = vicarService.getAllVicars();
            return new ResponseEntity<>(vicars, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get current vicar
    @GetMapping("/current")
    public ResponseEntity<Vicar> getCurrentVicar() {
        try {
            Optional<Vicar> currentVicar = vicarService.getCurrentVicar();
            if (currentVicar.isPresent()) {
                return new ResponseEntity<>(currentVicar.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get vicar by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vicar> getVicarById(@PathVariable String id) {
        try {
            Optional<Vicar> vicar = vicarService.getVicarById(id);
            if (vicar.isPresent()) {
                return new ResponseEntity<>(vicar.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update vicar
    @PutMapping("/{id}")
    public ResponseEntity<Vicar> updateVicar(@PathVariable String id, @RequestBody Vicar vicarDetails) {
        try {
            Vicar updatedVicar = vicarService.updateVicar(id, vicarDetails);
            if (updatedVicar != null) {
                return new ResponseEntity<>(updatedVicar, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete vicar
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteVicar(@PathVariable String id) {
        try {
            boolean deleted = vicarService.deleteVicar(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Vicar> uploadVicarImage(
            @PathVariable String id,
            @RequestParam("image") MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Vicar updatedVicar = vicarService.uploadVicarImage(id, imageFile);
            return new ResponseEntity<>(updatedVicar, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
