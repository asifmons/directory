package com.stjude.directory.controller;

import com.stjude.directory.dto.CreateMemberRequest;
import com.stjude.directory.dto.CreateFamilyRequest;
import com.stjude.directory.dto.FamilyResponseDTO;
import com.stjude.directory.service.FamilyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing family-related operations.
 * Provides endpoints for creating, updating, deleting, and retrieving family and family member details.
 */
@RestController
@RequestMapping("/api/families")
@Validated
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    /**
     * Creates a new family with the provided details.
     *
     * @param familyRequest the details of the family to be created
     * @return a ResponseEntity containing the created FamilyResponseDTO and an HTTP status of 201 (Created)
     * @throws Exception if there is an error during family creation
     */
    @PostMapping
    public ResponseEntity<FamilyResponseDTO> createFamily(
            @Valid CreateFamilyRequest familyRequest
    ) throws Exception {
        FamilyResponseDTO createdFamily = familyService.createFamily(familyRequest);
        return new ResponseEntity<>(createdFamily, HttpStatus.CREATED);
    }

    /**
     * Retrieves a family by its ID.
     *
     * @param id the ID of the family to be retrieved
     * @return a ResponseEntity containing the FamilyResponseDTO and an HTTP status of 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<FamilyResponseDTO> getFamily(@PathVariable String id) {
        FamilyResponseDTO family = familyService.getFamilyById(id);
        return ResponseEntity.ok(family);
    }

    /**
     * Retrieves a list of all families.
     *
     * @return a ResponseEntity containing a list of FamilyResponseDTOs and an HTTP status of 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<FamilyResponseDTO>> listFamilies() {
        List<FamilyResponseDTO> families = familyService.listAllFamilies();
        return ResponseEntity.ok(families);
    }

    /**
     * Updates an existing family with the provided details.
     *
     * @param id            the ID of the family to be updated
     * @param familyRequest the new details of the family
     * @return a ResponseEntity containing the updated FamilyResponseDTO and an HTTP status of 200 (OK)
     * @throws Exception if there is an error during family update
     */
    @PutMapping("/{id}")
    public ResponseEntity<FamilyResponseDTO> updateFamily(
            @PathVariable String id,
            @Valid @ModelAttribute CreateFamilyRequest familyRequest
    ) throws Exception {
        FamilyResponseDTO updatedFamily = familyService.updateFamily(id, familyRequest);
        return ResponseEntity.ok(updatedFamily);
    }

    /**
     * Deletes a family by its ID.
     *
     * @param id the ID of the family to be deleted
     * @return a ResponseEntity with an HTTP status of 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable String id) {
        familyService.deleteFamily(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds a new family member to the specified family.
     *
     * @param familyId     the ID of the family to which the member will be added
     * @param memberRequest the details of the member to be added
     * @return a ResponseEntity containing the updated FamilyResponseDTO and an HTTP status of 200 (OK)
     */
    @PostMapping("/{familyId}/members")
    public ResponseEntity<FamilyResponseDTO> addFamilyMember(
            @PathVariable String familyId,
            @Valid @RequestBody CreateMemberRequest memberRequest
    ) {
        FamilyResponseDTO updatedFamily = familyService.addFamilyMember(familyId, memberRequest);
        return ResponseEntity.ok(updatedFamily);
    }

    /**
     * Updates an existing family member's details.
     *
     * @param familyId     the ID of the family containing the member
     * @param memberId     the ID of the member to be updated
     * @param memberRequest the new details of the member
     * @return a ResponseEntity containing the updated FamilyResponseDTO and an HTTP status of 200 (OK)
     */
    @PutMapping("/{familyId}/members/{memberId}")
    public ResponseEntity<FamilyResponseDTO> updateFamilyMember(
            @PathVariable String familyId,
            @PathVariable String memberId,
            @Valid @RequestBody CreateMemberRequest memberRequest
    ) {
        FamilyResponseDTO updatedFamily = familyService.updateFamilyMember(familyId, memberId, memberRequest);
        return ResponseEntity.ok(updatedFamily);
    }

    /**
     * Deletes a family member from the specified family.
     *
     * @param familyId the ID of the family containing the member
     * @param memberId the ID of the member to be deleted
     * @return a ResponseEntity containing the updated FamilyResponseDTO and an HTTP status of 200 (OK)
     */
    @DeleteMapping("/{familyId}/members/{memberId}")
    public ResponseEntity<FamilyResponseDTO> deleteFamilyMember(
            @PathVariable String familyId,
            @PathVariable String memberId
    ) {
        FamilyResponseDTO updatedFamily = familyService.deleteFamilyMember(familyId, memberId);
        return ResponseEntity.ok(updatedFamily);
    }
}
