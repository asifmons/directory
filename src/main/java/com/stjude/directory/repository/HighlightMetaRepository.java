package com.stjude.directory.repository;

import com.stjude.directory.dto.HighlightMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HighlightMetaRepository extends MongoRepository<HighlightMeta, String> {
    Optional<HighlightMeta> findByHighlightId(String highlightId);
    Page<HighlightMeta> findAllByOrderByCreatedDateAsc(Pageable pageable);
}