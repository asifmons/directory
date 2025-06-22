package com.stjude.directory.repository;

import com.stjude.directory.enums.FeedbackReason;
import com.stjude.directory.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    
    // Find feedback by family ID
    List<Feedback> findByFamilyId(String familyId);
    
    // Find feedback by family ID with pagination
    Page<Feedback> findByFamilyId(String familyId, Pageable pageable);
    
    // Find feedback by family member ID
    List<Feedback> findByFamilyMemberId(String familyMemberId);
    
    // Find feedback by reason/category
    List<Feedback> findByReason(FeedbackReason reason);
    
    // Find feedback by family ID and reason
    List<Feedback> findByFamilyIdAndReason(String familyId, FeedbackReason reason);
    
    // Find all feedback ordered by creation date (newest first)
    List<Feedback> findAllByOrderByCreatedAtDesc();
    
    // Find all feedback with pagination ordered by creation date (newest first)
    Page<Feedback> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
