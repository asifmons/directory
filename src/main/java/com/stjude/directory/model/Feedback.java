package com.stjude.directory.model;

import com.stjude.directory.enums.FeedbackReason;
import com.stjude.directory.enums.Unit;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "feedbacks")
public class Feedback {
    
    @Id
    private String id;
    private String familyId;
    private String familyMemberId;
    private String familyMemberName; // Store member name for easy access
    private FeedbackReason reason;
    private String subject;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String familyHeadNumber;
    private Unit unit;// Assuming Unit is another class in your model

    // Default constructor
    public Feedback() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with parameters
    public Feedback(String familyId, String familyMemberId, String familyMemberName, FeedbackReason reason, String subject, String description, Unit unit) {
        this();
        this.familyId = familyId;
        this.familyMemberId = familyMemberId;
        this.familyMemberName = familyMemberName;
        this.reason = reason;
        this.subject = subject;
        this.description = description;
        this.unit = unit;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getFamilyMemberName() {
        return familyMemberName;
    }

    public void setFamilyMemberName(String familyMemberName) {
        this.familyMemberName = familyMemberName;
    }

    public FeedbackReason getReason() {
        return reason;
    }

    public void setReason(FeedbackReason reason) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id='" + id + '\'' +
                ", familyId='" + familyId + '\'' +
                ", familyMemberId='" + familyMemberId + '\'' +
                ", reason='" + reason + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
