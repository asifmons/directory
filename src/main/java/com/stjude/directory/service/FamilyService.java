package com.stjude.directory.service;

import com.stjude.directory.dto.CreateFamilyRequest;
import com.stjude.directory.dto.CreateMemberRequest;
import com.stjude.directory.dto.FamilyMemberResponseDTO;
import com.stjude.directory.dto.FamilyResponseDTO;
import com.stjude.directory.model.Family;
import com.stjude.directory.model.FamilyMember;
import com.stjude.directory.repository.FamilyRepository;
import com.stjude.directory.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing family entities and their members.
 * This class provides methods to create, retrieve, update, and delete families and family members,
 * as well as handle photo uploads to S3.
 */
@Service
public class FamilyService {

    private final FamilyRepository familyRepository;

    private final MemberRepository memberRepository;

    private final S3Service s3Service;


    public FamilyService(FamilyRepository familyRepository, MemberRepository memberRepository, S3Service s3Service) {
        this.familyRepository = familyRepository;
        this.memberRepository = memberRepository;
        this.s3Service = s3Service;
    }

    /**
     * Creates a new family with the provided request data.
     *
     * @param familyRequest the request data containing family details
     * @return the created FamilyResponseDTO containing the family details
     * @throws Exception if an error occurs during family creation
     */
    @Transactional
    public FamilyResponseDTO createFamily(CreateFamilyRequest familyRequest) throws Exception {
        Family family = new Family(familyRequest);
        // Handle photo upload
        if (familyRequest.getPhoto() != null && !familyRequest.getPhoto().isEmpty()) {
            String fileName = s3Service.uploadImage(familyRequest.getPhoto());
            String photoUrl = s3Service.generatePresignedUrl(fileName);
            family.setPhotoUrl(photoUrl);
        }
        family.setFamilyMembers(saveAndGetMembersFromFamily(familyRequest));
        Family savedFamily = familyRepository.save(family);
        return new FamilyResponseDTO(savedFamily);
    }

    private List<FamilyMember> saveAndGetMembersFromFamily(CreateFamilyRequest familyRequest) {
        if (familyRequest.getFamilyMembers() == null || familyRequest.getFamilyMembers().isEmpty()) {
            return Collections.emptyList();
        }
        List<FamilyMember> members = familyRequest.getFamilyMembers()
                .stream()
                .map(dto -> new FamilyMember(dto, familyRequest.getAddress(), familyRequest.getUnit()))
                .toList();
        memberRepository.saveAll(members);
        return members;
    }

    /**
     * Adds a new family member to a specified family.
     *
     * @param familyId      the ID of the family to which the member will be added
     * @param memberRequest the request data for the new family member
     * @return the updated FamilyResponseDTO containing the family details
     */
    @Transactional
    public FamilyResponseDTO addFamilyMember(String familyId, CreateMemberRequest memberRequest) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found"));
        FamilyMember newMember = new FamilyMember(memberRequest, family.getAddress(), family.getUnit());
        memberRepository.save(newMember);
        family.getFamilyMembers().add(newMember);
        Family updatedFamily = familyRepository.save(family);
        return new FamilyResponseDTO(updatedFamily);
    }

    /**
     * Retrieves a family by its unique ID.
     *
     * @param id the ID of the family to retrieve
     * @return the FamilyResponseDTO containing the family details
     * @throws RuntimeException if the family is not found
     */
    public FamilyResponseDTO getFamilyById(String id) {
        Family family = familyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family not found"));

        return convertToResponseDTO(family);
    }

    /**
     * Lists all families stored in the database.
     *
     * @return a list of FamilyResponseDTO representing all families
     */
    public List<FamilyResponseDTO> listAllFamilies() {
        List<Family> families = familyRepository.findAll();
        return families.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    /**
     * Updates an existing family's information.
     *
     * @param id            the ID of the family to update
     * @param familyRequest the request data containing updated family details
     * @return the updated FamilyResponseDTO with the family's details
     * @throws Exception if an error occurs during the update process
     */
    public FamilyResponseDTO updateFamily(String id, CreateFamilyRequest familyRequest) throws Exception {
        Family existingFamily = familyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family not found"));

        //existingFamily.setName(familyRequest.getName());
        existingFamily.setAddress(familyRequest.getAddress());
        existingFamily.setAnniversaryDate(familyRequest.getAnniversaryDate());

        // Handle photo upload
        if (familyRequest.getPhoto() != null && !familyRequest.getPhoto().isEmpty()) {
            String fileName = s3Service.uploadImage(familyRequest.getPhoto());
            String photoUrl = s3Service.generatePresignedUrl(fileName);
            existingFamily.setPhotoUrl(photoUrl);
        }

        // Update family members if provided
        if (familyRequest.getFamilyMembers() != null) {
            existingFamily.getFamilyMembers().clear();
            List<FamilyMember> updatedMembers = familyRequest.getFamilyMembers().stream().map(dto -> {
                FamilyMember member = new FamilyMember();
                member.setId(UUID.randomUUID().toString());
                member.setName(dto.getName());
                member.setDob(dto.getDob());
                member.setPhoneNumber(dto.getPhoneNumber());
                return member;
            }).toList();
            existingFamily.getFamilyMembers().addAll(updatedMembers);
        }

        // Save updated family
        Family updatedFamily = familyRepository.save(existingFamily);

        // Convert to FamilyResponseDTO
        return convertToResponseDTO(updatedFamily);
    }

    /**
     * Deletes a family identified by its ID.
     *
     * @param id the ID of the family to delete
     * @throws RuntimeException if the family is not found
     */
    public void deleteFamily(String id) {
        Family family = familyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family not found"));

        // Optionally, delete the photo from S3
        if (family.getPhotoUrl() != null) {
            String key = extractS3KeyFromUrl(family.getPhotoUrl());
            s3Service.deleteFileFromS3(key);
        }

        // Delete the family from MongoDB
        familyRepository.deleteById(id);
    }



    /**
     * Updates an existing family member's information.
     *
     * @param familyId      the ID of the family to which the member belongs
     * @param memberId      the ID of the member to update
     * @param memberRequest the request data for updating the family member
     * @return the updated FamilyResponseDTO containing the family's details
     * @throws RuntimeException if the family or member is not found
     */
    public FamilyResponseDTO updateFamilyMember(String familyId, String memberId, CreateMemberRequest memberRequest) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found"));

        Optional<FamilyMember> memberOpt = family.getFamilyMembers().stream()
                .filter(member -> member.getId().equals(memberId))
                .findFirst();

        if (memberOpt.isEmpty()) {
            throw new RuntimeException("Family member not found");
        }

        FamilyMember member = memberOpt.get();
        member.setName(memberRequest.getName());
        member.setDob(memberRequest.getDob());
        member.setPhoneNumber(memberRequest.getPhoneNumber());

        Family updatedFamily = familyRepository.save(family);

        return convertToResponseDTO(updatedFamily);
    }

    /**
     * Deletes a family member from a specified family.
     *
     * @param familyId the ID of the family from which the member will be deleted
     * @param memberId the ID of the member to delete
     * @return the updated FamilyResponseDTO containing the family's details
     * @throws RuntimeException if the family or member is not found
     */
    public FamilyResponseDTO deleteFamilyMember(String familyId, String memberId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found"));

        boolean removed = family.getFamilyMembers().removeIf(member -> member.getId().equals(memberId));

        if (!removed) {
            throw new RuntimeException("Family member not found");
        }

        Family updatedFamily = familyRepository.save(family);

        return convertToResponseDTO(updatedFamily);
    }

    /**
     * Uploads a file to S3 and returns the URL of the uploaded file.
     *
     * @param file the file to upload
     * @return the URL of the uploaded file
     * @throws Exception if an error occurs during the upload process
     */
//    private String uploadFileToS3(MultipartFile file) throws Exception {
//        String key = "family-photos/" + UUID.randomUUID();
//
//        s3Client.putObject(
//                PutObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(key)
//                        .build(),
//                RequestBody.fromBytes(file.getBytes())
//        );
//
//        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
//    }

    /**
     * Deletes a specified file from S3.
     *
     * @param key the S3 key of the file to delete
     */
//    private void deleteFileFromS3(String key) {
//        s3Client.deleteObject(
//                DeleteObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(key)
//                        .build()
//        );
//    }

    /**
     * Extracts the S3 key from the provided S3 URL.
     *
     * @param url the S3 URL
     * @return the extracted key
     */
    private String extractS3KeyFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * Converts a Family object to a FamilyResponseDTO.
     *
     * @param family the Family object to convert
     * @return the converted FamilyResponseDTO
     */
    private FamilyResponseDTO convertToResponseDTO(Family family) {
        FamilyResponseDTO response = new FamilyResponseDTO();
        response.setId(family.getId());
       // response.setName(family.getName());
        response.setAddress(family.getAddress());
        response.setAnniversaryDate(family.getAnniversaryDate());
        response.setPhotoUrl(family.getPhotoUrl());

        List<FamilyMemberResponseDTO> memberResponses = family.getFamilyMembers().stream()
                .map(member -> {
                    FamilyMemberResponseDTO memberResponse = new FamilyMemberResponseDTO();
                    memberResponse.setId(member.getId());
                    memberResponse.setName(member.getName());
                    memberResponse.setDob(member.getDob());
                    memberResponse.setPhoneNumber(member.getPhoneNumber());
                    memberResponse.setBloodGroup(member.getBloodGroup());
                    return memberResponse;
                })
                .toList();

        response.setFamilyMembers(memberResponses);
        return response;
    }
}
