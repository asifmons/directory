package com.stjude.directory.repository;

import com.stjude.directory.model.Family;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FamilyRepository extends MongoRepository<Family, String> {
}

