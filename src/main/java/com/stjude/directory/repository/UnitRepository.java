package com.stjude.directory.repository;

import com.stjude.directory.model.Unit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends MongoRepository<Unit, String> {
}