package com.stjude.directory.repository;

import com.stjude.directory.model.AnniversaryEvent;
import com.stjude.directory.model.Family;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends MongoRepository<Family, String> {

    @Aggregation(pipeline = {
        "{ $addFields: { 'anniversaryDatesArray': { $objectToArray: '$anniversaryDates' } } }",
        "{ $unwind: '$anniversaryDatesArray' }",
        "{ $addFields: { 'anniversaryDate': '$anniversaryDatesArray.v', 'coupleNo': { $toShort: '$anniversaryDatesArray.k' } } }",
        "{ $addFields: { 'today': new Date() } }",
        "{ $addFields: { 'anniversaryMonth': { $month: '$anniversaryDate' }, 'anniversaryDay': { $dayOfMonth: '$anniversaryDate' } } }",
        "{ $addFields: { 'nextWeek': { $add: ['$today', 7 * 24 * 60 * 60 * 1000] } } }",
        "{ $addFields: { 'anniversaryThisYear': { $dateFromParts: { 'year': { $year: '$today' }, 'month': '$anniversaryMonth', 'day': '$anniversaryDay' } } } }",
        "{ $match: { $expr: { $and: [ { $gte: ['$anniversaryThisYear', '$today'] }, { $lt: ['$anniversaryThisYear', '$nextWeek'] } ] } } }",
        "{ $lookup: { from: 'MEMBER', localField: 'id', foreignField: 'familyId', as: 'members' } }",
        "{ $addFields: { 'couple': { $filter: { input: '$members', as: 'member', cond: { $eq: ['$$member.coupleNo', '$coupleNo'] } } } } }",
        "{ $project: { 'houseName': 1, 'anniversaryDate': 1, 'id': 1, 'couple': { 'memberId': '$couple.id', 'name': '$couple.name' } } }"
    })
    List<AnniversaryEvent> findUpcomingAnniversaries();
    Optional<Family> findByAathmaSthithiNumber(String athamsthithiNumber);

}
