package com.stjude.directory.repository;

import com.stjude.directory.model.TermsAcceptance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TermsAcceptanceRepository extends MongoRepository<TermsAcceptance, String> {
    
    Optional<TermsAcceptance> findByFamilyId(String familyId);
    
    boolean existsByFamilyId(String familyId);
    
    Optional<TermsAcceptance> findByFamilyHeadId(String familyHeadId);
    
    boolean existsByFamilyHeadId(String familyHeadId);
}
