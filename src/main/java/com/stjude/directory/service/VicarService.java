package com.stjude.directory.service;

import com.stjude.directory.model.Vicar;
import com.stjude.directory.repository.VicarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VicarService {

    @Autowired
    private VicarRepository vicarRepository;

    @Autowired
    private S3Service s3Service; // Assuming you have a service for handling S3 uploads

    // Add a new vicar
    public Vicar addVicar(Vicar vicar) {
        // If this vicar is marked as present, make sure no other vicar is marked as present
        if (vicar.isPresent()) {
            List<Vicar> currentVicars = vicarRepository.findAll();
            for (Vicar v : currentVicars) {
                if (v.isPresent()) {
                    v.setPresent(false);
                    vicarRepository.save(v);
                }
            }
        }
        return vicarRepository.save(vicar);
    }

    // Get all vicars ordered by start date
    public List<Vicar> getAllVicars() {
        return vicarRepository.findAllByOrderByStartDateAsc();
    }

    // Get current vicar
    public Optional<Vicar> getCurrentVicar() {
        return vicarRepository.findByIsPresentTrue();
    }

    // Get vicar by ID
    public Optional<Vicar> getVicarById(String id) {
        return vicarRepository.findById(id);
    }

    // Update vicar
    public Vicar updateVicar(String id, Vicar vicarDetails) {
        Optional<Vicar> optionalVicar = vicarRepository.findById(id);
        if (optionalVicar.isPresent()) {
            Vicar vicar = optionalVicar.get();
            vicar.setName(vicarDetails.getName());
            vicar.setStartDate(vicarDetails.getStartDate());
            vicar.setEndDate(vicarDetails.getEndDate());
            vicar.setImageUrl(vicarDetails.getImageUrl());

            // Handle present vicar logic
            if (vicarDetails.isPresent() && !vicar.isPresent()) {
                // If setting this vicar as present, remove present status from others
                List<Vicar> currentVicars = vicarRepository.findAll();
                for (Vicar v : currentVicars) {
                    if (v.isPresent() && !v.getId().equals(id)) {
                        v.setPresent(false);
                        vicarRepository.save(v);
                    }
                }
            }
            vicar.setPresent(vicarDetails.isPresent());

            return vicarRepository.save(vicar);
        }
        return null;
    }

    // Delete vicar
    public boolean deleteVicar(String id) {
        if (vicarRepository.existsById(id)) {
            vicarRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get vicars who served on a specific date
    public List<Vicar> getVicarsByDate(LocalDate date) {
        return vicarRepository.findVicarsByDate(date);
    }

    public Vicar uploadVicarImage(String vicarId, MultipartFile imageFile) throws IOException {
        // Check if vicar exists
        Optional<Vicar> optionalVicar = vicarRepository.findById(vicarId);
        if (optionalVicar.isEmpty()) {
            throw new IllegalArgumentException("Vicar not found with ID: " + vicarId);
        }

        Vicar vicar = optionalVicar.get();

        // Generate unique filename to avoid collisions
        ;

        // Define upload directory path (you might want to inject this from application properties)
        String fileName = s3Service.uploadVicarImage(imageFile);
        String s3url = s3Service.generatePublicUrl(fileName);
        vicar.setImageUrl(s3url);

        // Update vicar with image URL

        // Save the updated vicar
        return vicarRepository.save(vicar);
    }
}
