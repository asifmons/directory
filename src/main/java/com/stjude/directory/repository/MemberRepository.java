package com.stjude.directory.repository;

import com.stjude.directory.model.FamilyMember;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<FamilyMember, String> {
}

