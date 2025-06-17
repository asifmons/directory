package com.stjude.directory.repository;

import com.stjude.directory.model.ParishCouncil;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParishCouncilRepository extends MongoRepository<ParishCouncil, String> {

    // Find all parish councils ordered by start year
    List<ParishCouncil> findAllByOrderByStartYearDesc();

    // Find active parish councils
    List<ParishCouncil> findByIsActiveTrue();

    // Find parish councils by year range
    @Query("{'startYear': {$lte: ?0}, 'endYear': {$gte: ?0}}")
    List<ParishCouncil> findParishCouncilsByYear(Integer year);

    // Find parish councils that started in a specific year
    List<ParishCouncil> findByStartYear(Integer startYear);

    // Find parish councils that ended in a specific year
    List<ParishCouncil> findByEndYear(Integer endYear);

    // Find current parish council (active and current year falls within range)
    @Query("{'isActive': true, 'startYear': {$lte: ?0}, 'endYear': {$gte: ?0}}")
    Optional<ParishCouncil> findCurrentParishCouncil(Integer currentYear);
} 