package com.stjude.directory.repository;

import com.stjude.directory.model.Family;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FamilyRepository extends MongoRepository<Family, String> {
}

