package com.stjude.directory.repository;

import com.stjude.directory.model.Vicar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VicarRepository extends MongoRepository<Vicar, String> {

    // Find all vicars ordered by start date
    List<Vicar> findAllByOrderByStartDateAsc();

    // Find current vicar
    Optional<Vicar> findByIsPresentTrue();

    // Find vicars by date range
    @Query("{'startDate': {$lte: ?0}, 'endDate': {$gte: ?0}}")
    List<Vicar> findVicarsByDate(LocalDate date);

    // Find vicars who served before a specific date
    List<Vicar> findByEndDateBefore(LocalDate date);

    // Find vicars who started after a specific date
    List<Vicar> findByStartDateAfter(LocalDate date);
}