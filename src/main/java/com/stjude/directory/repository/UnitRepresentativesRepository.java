package com.stjude.directory.repository;

import com.stjude.directory.model.UnitRepresentatives;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnitRepresentativesRepository extends MongoRepository<UnitRepresentatives, String> {
    Optional<UnitRepresentatives> findByUnitId(String unitId);
}
