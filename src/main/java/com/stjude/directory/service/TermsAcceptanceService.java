package com.stjude.directory.service;

import com.stjude.directory.model.Member;
import com.stjude.directory.model.TermsAcceptance;
import com.stjude.directory.repository.TermsAcceptanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TermsAcceptanceService {
    
    @Autowired
    private TermsAcceptanceRepository termsAcceptanceRepository;
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private FamilyService familyService;
    
    public TermsAcceptance acceptTerms(String familyId) {
        // Check if family has already accepted terms
        if (termsAcceptanceRepository.existsByFamilyId(familyId)) {
            throw new RuntimeException("Family has already accepted terms and conditions");
        }
        
        // Get family head details
        Member familyHead = getFamilyHead(familyId);
        if (familyHead == null) {
            throw new RuntimeException("Family head not found for family ID: " + familyId);
        }
        
        // Create and save terms acceptance record
        TermsAcceptance termsAcceptance = new TermsAcceptance(familyHead.getId(), familyId, familyHead.getName());
        return termsAcceptanceRepository.save(termsAcceptance);
    }
    
    public Optional<TermsAcceptance> getTermsAcceptanceByFamilyId(String familyId) {
        return termsAcceptanceRepository.findByFamilyId(familyId);
    }
    
    public boolean hasFamilyAcceptedTerms(String familyId) {
        return termsAcceptanceRepository.existsByFamilyId(familyId);
    }
    
    private Member getFamilyHead(String familyId) {
        // Get all members of the family and find the family head
        return memberService.getMembersByFamilyId(familyId)
                .stream()
                .filter(member -> Boolean.TRUE.equals(member.getIsFamilyHead()))
                .findFirst()
                .orElse(null);
    }
}
