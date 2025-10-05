package com.stjude.directory.repository;

import com.stjude.directory.model.Member;
import com.stjude.directory.model.BirthdayEvent;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<com.stjude.directory.model.Member, String> {
    List<com.stjude.directory.model.Member> findByFamilyId(String familyId);

    @Query("{'id': ?0, 'familyId': ?1}")
    Optional<com.stjude.directory.model.Member> findMemberByFamilyIdAndMemberId(String memberId, String familyId);

    List<com.stjude.directory.model.Member> findByFamilyIdAndCoupleNo(String familyId, Short coupleNo);

    @Aggregation(pipeline = {
        "{ $addFields: { 'today': new Date() } }",
        "{ $addFields: { 'todayMonth': { $month: '$today' }, 'todayDay': { $dayOfMonth: '$today' } } }",
        "{ $addFields: { 'birthMonth': { $month: '$dob' }, 'birthDay': { $dayOfMonth: '$dob' } } }",
        "{ $addFields: { 'nextWeek': { $add: ['$today', 7 * 24 * 60 * 60 * 1000] } } }",
        "{ $addFields: { 'birthDateThisYear': { $dateFromParts: { 'year': { $year: '$today' }, 'month': '$birthMonth', 'day': '$birthDay' } } } }",
        "{ $match: { $expr: { $and: [ { $gte: ['$birthDateThisYear', '$today'] }, { $lt: ['$birthDateThisYear', '$nextWeek'] } ] } } }",
        "{ $project: { 'name': 1, 'dob': 1, 'id': 1, 'familyId': 1 } }"
    })
    List<BirthdayEvent> findUpcomingBirthdays();
}
