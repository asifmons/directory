package com.stjude.directory.service;

import com.stjude.directory.dto.ParishCouncilRequest;
import com.stjude.directory.dto.ParishCouncilResponse;
import com.stjude.directory.model.ParishCouncil;
import com.stjude.directory.repository.ParishCouncilRepository;
import com.stjude.directory.utils.StringOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParishCouncilService {

    @Autowired
    private ParishCouncilRepository parishCouncilRepository;

    @Autowired
    private S3Service s3Service;

    // Add a new parish council
    public ParishCouncilResponse addParishCouncil(ParishCouncilRequest request) {
        // Validate year range
        if (request.getStartYear() > request.getEndYear()) {
            throw new IllegalArgumentException("Start year cannot be greater than end year");
        }

        // If this parish council is marked as active, deactivate others
        if (request.isActive()) {
            List<ParishCouncil> activeCouncils = parishCouncilRepository.findByIsActiveTrue();
            for (ParishCouncil council : activeCouncils) {
                council.setActive(false);
                parishCouncilRepository.save(council);
            }
        }

        ParishCouncil parishCouncil = new ParishCouncil();
        parishCouncil.setId(StringOps.generateUUID());
        parishCouncil.setStartYear(request.getStartYear());
        parishCouncil.setEndYear(request.getEndYear());
        parishCouncil.setActive(request.isActive());
        parishCouncil.setCreatedAt(LocalDateTime.now());
        parishCouncil.setUpdatedAt(LocalDateTime.now());

        ParishCouncil savedCouncil = parishCouncilRepository.save(parishCouncil);
        return convertToResponse(savedCouncil);
    }

    // Get all parish councils
    public List<ParishCouncilResponse> getAllParishCouncils() {
        List<ParishCouncil> councils = parishCouncilRepository.findAllByOrderByStartYearDesc();
        return councils.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get parish council by ID
    public Optional<ParishCouncilResponse> getParishCouncilById(String id) {
        Optional<ParishCouncil> council = parishCouncilRepository.findById(id);
        return council.map(this::convertToResponse);
    }

    // Get current parish council
    public Optional<ParishCouncilResponse> getCurrentParishCouncil() {
        int currentYear = LocalDateTime.now().getYear();
        Optional<ParishCouncil> council = parishCouncilRepository.findCurrentParishCouncil(currentYear);
        return council.map(this::convertToResponse);
    }

    // Get parish councils by year
    public List<ParishCouncilResponse> getParishCouncilsByYear(Integer year) {
        List<ParishCouncil> councils = parishCouncilRepository.findParishCouncilsByYear(year);
        return councils.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Update parish council
    public Optional<ParishCouncilResponse> updateParishCouncil(String id, ParishCouncilRequest request) {
        Optional<ParishCouncil> optionalCouncil = parishCouncilRepository.findById(id);
        if (optionalCouncil.isPresent()) {
            ParishCouncil council = optionalCouncil.get();
            
            // Validate year range
            if (request.getStartYear() > request.getEndYear()) {
                throw new IllegalArgumentException("Start year cannot be greater than end year");
            }

            council.setStartYear(request.getStartYear());
            council.setEndYear(request.getEndYear());
            council.setUpdatedAt(LocalDateTime.now());

            // Handle active status
            if (request.isActive() && !council.isActive()) {
                // If setting this council as active, deactivate others
                List<ParishCouncil> activeCouncils = parishCouncilRepository.findByIsActiveTrue();
                for (ParishCouncil activeCouncil : activeCouncils) {
                    if (!activeCouncil.getId().equals(id)) {
                        activeCouncil.setActive(false);
                        parishCouncilRepository.save(activeCouncil);
                    }
                }
            }
            council.setActive(request.isActive());

            ParishCouncil updatedCouncil = parishCouncilRepository.save(council);
            return Optional.of(convertToResponse(updatedCouncil));
        }
        return Optional.empty();
    }

    // Delete parish council
    public boolean deleteParishCouncil(String id) {
        if (parishCouncilRepository.existsById(id)) {
            parishCouncilRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Upload parish council image
    public Optional<ParishCouncilResponse> uploadParishCouncilImage(String councilId, MultipartFile imageFile) throws IOException {
        Optional<ParishCouncil> optionalCouncil = parishCouncilRepository.findById(councilId);
        if (optionalCouncil.isEmpty()) {
            throw new IllegalArgumentException("Parish council not found with ID: " + councilId);
        }

        ParishCouncil council = optionalCouncil.get();

        // Upload image to S3
        String fileName = s3Service.uploadParishCouncilImage(imageFile);
        String s3Url = s3Service.generatePublicUrl(fileName);
        
        council.setImageUrl(s3Url);
        council.setUpdatedAt(LocalDateTime.now());

        ParishCouncil updatedCouncil = parishCouncilRepository.save(council);
        return Optional.of(convertToResponse(updatedCouncil));
    }

    // Convert ParishCouncil to ParishCouncilResponse
    private ParishCouncilResponse convertToResponse(ParishCouncil council) {
        ParishCouncilResponse response = new ParishCouncilResponse();
        response.setId(council.getId());
        response.setImageUrl(council.getImageUrl());
        response.setStartYear(council.getStartYear());
        response.setEndYear(council.getEndYear());
        response.setActive(council.isActive());
        response.setCreatedAt(council.getCreatedAt());
        response.setUpdatedAt(council.getUpdatedAt());
        return response;
    }
} 