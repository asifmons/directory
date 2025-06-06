package com.stjude.directory.repository;

import com.stjude.directory.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    List<Member> findByFamilyId(String familyId);

    @Query("{'id': ?0, 'familyId': ?1}")
    Optional<Member> findMemberByFamilyIdAndMemberId(String memberId, String familyId);
}

