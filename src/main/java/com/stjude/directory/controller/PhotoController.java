package com.stjude.directory.controller;

import com.stjude.directory.dto.PreviewPageResponse;
import com.stjude.directory.dto.YearPreviewPageResponse;
import com.stjude.directory.service.PhotoService;
import com.stjude.directory.service.S3Service;
import com.stjude.directory.model.YearWisePhoto;
import com.stjude.directory.repository.YearWisePhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    private final S3Service s3Service;
    private final YearWisePhotoRepository yearWisePhotoRepository;
    private final PhotoService photoService;

    @Autowired
    public PhotoController(S3Service s3Service,
                           YearWisePhotoRepository yearWisePhotoRepository,
                           PhotoService photoService) {
        this.s3Service = s3Service;
        this.yearWisePhotoRepository = yearWisePhotoRepository;
        this.photoService = photoService;
    }

    // Upload multiple photos for a specific year
    @PostMapping("/{year}")
    public ResponseEntity<String> uploadPhotos(
            @PathVariable int year,
            @RequestParam("files") List<MultipartFile> files
    ) {
        int successCount = 0;
        for (MultipartFile file : files) {
            try {
                String fileName = s3Service.uploadImageWithCustomKey(file);
                String url = s3Service.generatePublicUrl(fileName);
                YearWisePhoto yw = new YearWisePhoto(year, url, file.getOriginalFilename(), LocalDateTime.now());
                yearWisePhotoRepository.save(yw);
                successCount++;
            } catch (IOException e) {
                // handle individual failure if needed
            }
        }
        if (successCount == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload any photos for year " + year);
        }
        return ResponseEntity.ok(successCount + " photo(s) uploaded successfully for year " + year);
    }

    // Get paginated photos for a given year
    @GetMapping("/{year}")
    public ResponseEntity<PreviewPageResponse> getPaginatedPhotos(
            @PathVariable int year,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(photoService.getPreviewPhotos(year, page, size));
    }

    // Paginated preview across years: default 10 years/page, 4 photos/year
    @GetMapping("/preview")
    public ResponseEntity<YearPreviewPageResponse> previewAcrossYears(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(photoService.getPreviewAcrossYears(page, size));
    }
}
