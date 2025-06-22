package com.stjude.directory.controller;

import com.stjude.directory.dto.ApiResponse;
import com.stjude.directory.dto.FeedbackRequest;
import com.stjude.directory.model.Feedback;
import com.stjude.directory.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * Submit feedback - Main endpoint for Flutter app
     * POST /api/feedback
     * 
     * Expected request body:
     * {
     *   "familyId": "string",
     *   "familyMemberId": "string", // optional
     *   "reason": "string",
     *   "subject": "string", 
     *   "description": "string"
     * }
     */
    @PostMapping("/feedback")
    public ResponseEntity<ApiResponse<Feedback>> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        try {
            Feedback feedback = feedbackService.createFeedback(request);
            
            ApiResponse<Feedback> response = ApiResponse.success("Feedback submitted successfully", feedback);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            ApiResponse<Feedback> response = ApiResponse.error("Failed to submit feedback: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get feedback by family ID (Optional endpoint for admin)
     * GET /api/feedback/family/{familyId}
     */
    @GetMapping("/feedback/family/{familyId}")
    public ResponseEntity<ApiResponse<java.util.List<Feedback>>> getFeedbackByFamilyId(@PathVariable String familyId) {
        try {
            java.util.List<Feedback> feedbackList = feedbackService.getFeedbackByFamilyId(familyId);
            
            ApiResponse<java.util.List<Feedback>> response = ApiResponse.success("Family feedback retrieved successfully", feedbackList);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<java.util.List<Feedback>> response = ApiResponse.error("Failed to retrieve family feedback: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get feedback statistics (Optional endpoint for admin)
     * GET /api/feedback/stats
     */
    @GetMapping("/feedback/stats")
    public ResponseEntity<ApiResponse<java.util.Map<String, Object>>> getFeedbackStats() {
        try {
            long totalCount = feedbackService.getTotalFeedbackCount();
            
            java.util.Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("totalFeedback", totalCount);
            stats.put("message", "Total feedback count retrieved successfully");
            
            ApiResponse<java.util.Map<String, Object>> response = ApiResponse.success("Feedback statistics retrieved successfully", stats);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<java.util.Map<String, Object>> response = ApiResponse.error("Failed to retrieve feedback statistics: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
