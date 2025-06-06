package com.stjude.directory.service;

import com.stjude.directory.dto.PhotoItemDto;
import com.stjude.directory.dto.PreviewPageResponse;
import com.stjude.directory.dto.YearPhotoPreview;
import com.stjude.directory.dto.YearPreviewPageResponse;
import com.stjude.directory.model.YearWisePhoto;
import com.stjude.directory.repository.YearWisePhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    private final YearWisePhotoRepository yearWisePhotoRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PhotoService(YearWisePhotoRepository yearWisePhotoRepository,
                        MongoTemplate mongoTemplate) {
        this.yearWisePhotoRepository = yearWisePhotoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public PreviewPageResponse getPreviewPhotos(int year, int page, int size) {
        Page<YearWisePhoto> photoPage = yearWisePhotoRepository.findByYear(
                year, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "uploadedAt")));
        int total = (int) yearWisePhotoRepository.countByYear(year);
        List<PhotoItemDto> items = photoPage.getContent().stream()
                .map(p -> new PhotoItemDto(p.getId(), p.getUrl()))
                .collect(Collectors.toList());
        return new PreviewPageResponse(page, size, total, items);
    }

    public YearPreviewPageResponse getPreviewAcrossYears(int page, int size) {
        List<Integer> years = yearWisePhotoRepository.findDistinctYearsPaginated(page * size, size);
        int totalYears = mongoTemplate.query(YearWisePhoto.class)
                .distinct("year").as(Integer.class).all().size();
        List<YearPhotoPreview> previews = years.stream()
                .map(y -> {
                    List<PhotoItemDto> items = yearWisePhotoRepository
                            .findTop4ByYearOrderByUploadedAtDesc(y)
                            .stream()
                            .map(p -> new PhotoItemDto(p.getId(), p.getUrl()))
                            .collect(Collectors.toList());
                    return new YearPhotoPreview(y, items);
                })
                .collect(Collectors.toList());
        return new YearPreviewPageResponse(page, size, totalYears, previews);
    }
}
