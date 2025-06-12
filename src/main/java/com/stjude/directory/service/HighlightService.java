package com.stjude.directory.service;

import com.stjude.directory.dto.HighlightImage;
import com.stjude.directory.dto.HighlightMeta;
import com.stjude.directory.dto.HighlightPreviewResponse;
import com.stjude.directory.dto.PagedResponse;
import com.stjude.directory.repository.HighlightImageRepository;
import com.stjude.directory.repository.HighlightMetaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class HighlightService {
    private static final int PREVIEW_IMAGE_COUNT = 5;

    private final HighlightImageRepository imageRepository;
    private final HighlightMetaRepository metaRepository;
    private final S3Service s3Service;

    public HighlightService(HighlightImageRepository imageRepository,
                            HighlightMetaRepository metaRepository,
                            S3Service s3Service) {
        this.imageRepository = imageRepository;
        this.metaRepository = metaRepository;
        this.s3Service = s3Service;
    }

    public String createHighlight(String name, List<MultipartFile> images) throws Exception {
        validateCreateRequest(name, images);

        String highlightId = UUID.randomUUID().toString();


        try {
            // Create and save metadata first
            HighlightMeta meta = new HighlightMeta(highlightId, name);
            metaRepository.save(meta);

            // Upload and save images
            saveImages(highlightId, images);

            // Update metadata with image counts and preview URLs
            updateMetadataWithImageInfo(highlightId);

            return highlightId;

        } catch (Exception e) {
            // TODO: Implement rollback logic for S3 cleanup
           throw new Exception("Failed to create highlight: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public PagedResponse<HighlightPreviewResponse> getHighlightPreviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HighlightMeta> metaPage = metaRepository.findAllByOrderByCreatedDateAsc(pageable);

        Page<HighlightPreviewResponse> responsePage = metaPage.map(meta ->
                new HighlightPreviewResponse(
                        meta.getHighlightId(),
                        meta.getName(),
                        meta.getCreatedDate(),
                        meta.getTotalImages(),
                        meta.getPreviewImageUrls()
                )
        );

        return new PagedResponse<>(responsePage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<String> getHighlightImages(String highlightId, int page, int size) throws Exception {
        if (!highlightExists(highlightId)) {
            throw new Exception("Highlight not found: " + highlightId);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<HighlightImage> imagePage = imageRepository
                .findByHighlightIdOrderByUploadDateAsc(highlightId, pageable);

        Page<String> urlPage = imagePage.map(HighlightImage::getS3Url);
        return new PagedResponse<>(urlPage);
    }

    private void validateCreateRequest(String name, List<MultipartFile> images) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Highlight name is required");
        }
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("At least one image is required");
        }
    }

    private void saveImages(String highlightId, List<MultipartFile> images) throws IOException {
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                String fileName = s3Service.uploadHighLightImage(image);
                String s3Url = s3Service.generatePublicUrl(fileName);
                HighlightImage highlightImage = new HighlightImage(
                        highlightId, s3Url, image.getOriginalFilename());
                imageRepository.save(highlightImage);
            }
        }
    }

    private void updateMetadataWithImageInfo(String highlightId) {
        List<HighlightImage> previewImages = imageRepository
                .findTop5ByHighlightIdOrderByUploadDateAsc(highlightId);

        List<String> previewUrls = previewImages.stream()
                .map(HighlightImage::getS3Url)
                .toList();

        HighlightMeta meta = metaRepository.findByHighlightId(highlightId)
                .orElseThrow(() -> new IllegalStateException("Metadata not found for highlight: " + highlightId));

        meta.updateImageCounts(previewImages.size(), previewUrls);
        metaRepository.save(meta);
    }

    private boolean highlightExists(String highlightId) {
        return metaRepository.findByHighlightId(highlightId).isPresent();
    }
}