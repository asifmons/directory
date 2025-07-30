package com.stjude.directory.repository;

import com.stjude.directory.model.Position;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PositionRepository extends MongoRepository<Position, String> {
}
