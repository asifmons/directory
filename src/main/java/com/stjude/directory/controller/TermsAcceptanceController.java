package com.stjude.directory.controller;

import com.stjude.directory.dto.ApiResponse;
import com.stjude.directory.model.TermsAcceptance;
import com.stjude.directory.service.TermsAcceptanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/terms")
public class TermsAcceptanceController {
    
    @Autowired
    private TermsAcceptanceService termsAcceptanceService;
    
    /**
     * Accept terms and conditions for a family
     * 
     * @param familyId the ID of the family accepting terms
     * @return ResponseEntity with the terms acceptance record
     */
    @PostMapping("/accept/{familyId}")
    public ResponseEntity<ApiResponse<TermsAcceptance>> acceptTerms(@PathVariable String familyId) {
        try {
            TermsAcceptance termsAcceptance = termsAcceptanceService.acceptTerms(familyId);
            ApiResponse<TermsAcceptance> response = ApiResponse.success(
                "Terms and conditions accepted successfully", 
                termsAcceptance
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<TermsAcceptance> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Check if a family has accepted terms and conditions
     * 
     * @param familyId the ID of the family to check
     * @return ResponseEntity with acceptance status
     */
    @GetMapping("/status/{familyId}")
    public ResponseEntity<ApiResponse<Boolean>> getAcceptanceStatus(@PathVariable String familyId) {
        try {
            boolean hasAccepted = termsAcceptanceService.hasFamilyAcceptedTerms(familyId);
            String message = hasAccepted ? "Family has accepted terms and conditions" : "Family has not accepted terms and conditions";
            ApiResponse<Boolean> response = ApiResponse.success(message, hasAccepted);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Boolean> response = ApiResponse.error("Error checking terms acceptance status");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get terms acceptance details for a family
     * 
     * @param familyId the ID of the family
     * @return ResponseEntity with terms acceptance details
     */
    @GetMapping("/details/{familyId}")
    public ResponseEntity<ApiResponse<TermsAcceptance>> getTermsAcceptanceDetails(@PathVariable String familyId) {
        try {
            Optional<TermsAcceptance> termsAcceptance = termsAcceptanceService.getTermsAcceptanceByFamilyId(familyId);
            if (termsAcceptance.isPresent()) {
                ApiResponse<TermsAcceptance> response = ApiResponse.success(
                    "Terms acceptance details retrieved successfully", 
                    termsAcceptance.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<TermsAcceptance> response = ApiResponse.error("No terms acceptance record found for this family");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            ApiResponse<TermsAcceptance> response = ApiResponse.error("Error retrieving terms acceptance details");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
