package com.stjude.directory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FeedbackRequest {
    
    @NotBlank(message = "Family ID is required")
    private String familyId;
    
    private String familyMemberId; // Optional
    
    @NotBlank(message = "Reason is required")
    private String reason;
    
    @NotBlank(message = "Subject is required")
    @Size(max = 200, message = "Subject must not exceed 200 characters")
    private String subject;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    // Default constructor
    public FeedbackRequest() {}

    // Constructor with parameters
    public FeedbackRequest(String familyId, String familyMemberId, String reason, String subject, String description) {
        this.familyId = familyId;
        this.familyMemberId = familyMemberId;
        this.reason = reason;
        this.subject = subject;
        this.description = description;
    }

    // Getters and Setters
    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(String familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FeedbackRequest{" +
                "familyId='" + familyId + '\'' +
                ", familyMemberId='" + familyMemberId + '\'' +
                ", reason='" + reason + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
