package com.stjude.directory.repository;

import com.stjude.directory.model.Committee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeRepository extends MongoRepository<Committee, String> {
}
