package com.stjude.directory.repository;

import com.stjude.directory.model.YearWisePhoto;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YearWisePhotoRepository extends MongoRepository<YearWisePhoto, String> {

    List<YearWisePhoto> findTop4ByYearOrderByUploadedAtDesc(int year);

    Page<YearWisePhoto> findByYear(int year, Pageable pageable);

    long countByYear(int year);

    @Aggregation(pipeline = {
        "{ '$group': { '_id': '$year' } }",
        "{ '$sort': { '_id': -1 } }",
        "{ '$skip': ?0 }",
        "{ '$limit': ?1 }",
        "{ '$project': { '_id': 0, 'year': '$_id' } }"
    })
    List<Integer> findDistinctYearsPaginated(int skip, int limit);
}
