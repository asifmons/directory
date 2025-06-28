package com.stjude.directory.repository;

import com.stjude.directory.dto.HighlightImage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighlightImageRepository extends MongoRepository<HighlightImage, String> {
    Page<HighlightImage> findByHighlightIdOrderByUploadDateAsc(String highlightId, Pageable pageable);
    List<HighlightImage> findTop5ByHighlightIdOrderByUploadDateAsc(String highlightId);
    List<HighlightImage> findByHighlightId(String highlightId);
    long countByHighlightId(String highlightId);
    void deleteByHighlightId(String highlightId);
}
