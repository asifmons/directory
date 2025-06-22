package com.stjude.directory.service;

import com.stjude.directory.dto.FeedbackRequest;
import com.stjude.directory.enums.FeedbackReason;
import com.stjude.directory.model.Feedback;
import com.stjude.directory.model.Member;
import com.stjude.directory.repository.FeedbackRepository;
import com.stjude.directory.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private MemberRepository memberRepository;

    /**
     * Create new feedback
     */
    public Feedback createFeedback(FeedbackRequest request) {
        // Convert string reason to enum
        FeedbackReason reason = FeedbackReason.fromDisplayName(request.getReason());
        
        // Fetch member name if member ID is provided
        String memberName = null;
        if (request.getFamilyMemberId() != null && !request.getFamilyMemberId().isEmpty()) {
            Optional<Member> member = memberRepository.findById(request.getFamilyMemberId());
            if (member.isPresent()) {
                memberName = member.get().getName();
            }
        }
        
        Feedback feedback = new Feedback(
            request.getFamilyId(),
            request.getFamilyMemberId(),
            memberName,
            reason,
            request.getSubject(),
            request.getDescription()
        );
        
        return feedbackRepository.save(feedback);
    }

    /**
     * Get all feedback with pagination
     */
    public Page<Feedback> getAllFeedback(Pageable pageable) {
        return feedbackRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    /**
     * Get feedback by ID
     */
    public Optional<Feedback> getFeedbackById(String id) {
        return feedbackRepository.findById(id);
    }

    /**
     * Get feedback by family ID
     */
    public List<Feedback> getFeedbackByFamilyId(String familyId) {
        return feedbackRepository.findByFamilyId(familyId);
    }

    /**
     * Get feedback by family ID with pagination
     */
    public Page<Feedback> getFeedbackByFamilyId(String familyId, Pageable pageable) {
        return feedbackRepository.findByFamilyId(familyId, pageable);
    }

    /**
     * Get feedback by family member ID
     */
    public List<Feedback> getFeedbackByFamilyMemberId(String familyMemberId) {
        return feedbackRepository.findByFamilyMemberId(familyMemberId);
    }

    /**
     * Get feedback by reason/category
     */
    public List<Feedback> getFeedbackByReason(String reason) {
        FeedbackReason feedbackReason = FeedbackReason.fromDisplayName(reason);
        return feedbackRepository.findByReason(feedbackReason);
    }

    /**
     * Get feedback by family ID and reason
     */
    public List<Feedback> getFeedbackByFamilyIdAndReason(String familyId, String reason) {
        FeedbackReason feedbackReason = FeedbackReason.fromDisplayName(reason);
        return feedbackRepository.findByFamilyIdAndReason(familyId, feedbackReason);
    }

    /**
     * Update feedback
     */
    public Optional<Feedback> updateFeedback(String id, FeedbackRequest request) {
        Optional<Feedback> existingFeedback = feedbackRepository.findById(id);
        
        if (existingFeedback.isPresent()) {
            Feedback feedback = existingFeedback.get();
            
            // Convert string reason to enum
            FeedbackReason reason = FeedbackReason.fromDisplayName(request.getReason());
            
            // Fetch member name if member ID is provided
            String memberName = null;
            if (request.getFamilyMemberId() != null && !request.getFamilyMemberId().isEmpty()) {
                Optional<Member> member = memberRepository.findById(request.getFamilyMemberId());
                if (member.isPresent()) {
                    memberName = member.get().getName();
                }
            }
            
            feedback.setFamilyId(request.getFamilyId());
            feedback.setFamilyMemberId(request.getFamilyMemberId());
            feedback.setFamilyMemberName(memberName);
            feedback.setReason(reason);
            feedback.setSubject(request.getSubject());
            feedback.setDescription(request.getDescription());
            feedback.setUpdatedAt(LocalDateTime.now());
            
            return Optional.of(feedbackRepository.save(feedback));
        }
        
        return Optional.empty();
    }

    /**
     * Delete feedback by ID
     */
    public boolean deleteFeedback(String id) {
        if (feedbackRepository.existsById(id)) {
            feedbackRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get total feedback count
     */
    public long getTotalFeedbackCount() {
        return feedbackRepository.count();
    }

    /**
     * Get feedback count by family ID
     */
    public long getFeedbackCountByFamilyId(String familyId) {
        return feedbackRepository.findByFamilyId(familyId).size();
    }

    /**
     * Get feedback count by reason
     */
    public long getFeedbackCountByReason(String reason) {
        FeedbackReason feedbackReason = FeedbackReason.fromDisplayName(reason);
        return feedbackRepository.findByReason(feedbackReason).size();
    }
}
