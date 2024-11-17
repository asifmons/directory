package com.stjude.directory.service;

import com.stjude.directory.CriteriaHelper;
import com.stjude.directory.dto.*;
import com.stjude.directory.enums.EvaluationType;
import com.stjude.directory.enums.Operation;
import com.stjude.directory.model.*;
import com.stjude.directory.repository.FamilyRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service class for managing family entities and their members.
 * This class provides methods to create, retrieve, update, and delete families and family members,
 * as well as handle photo uploads to S3.
 */
@Service
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final MemberService memberService;
    private final S3Service s3Service;
    private final MongoTemplate mongoTemplate;

    public FamilyService(FamilyRepository familyRepository, MemberService memberService,
                         S3Service s3Service, MongoTemplate mongoTemplate) {
        this.familyRepository = familyRepository;
        this.memberService = memberService;
        this.s3Service = s3Service;
        this.mongoTemplate = mongoTemplate;
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
            String photoUrl = s3Service.generatePublicUrl(fileName);
            family.setPhotoUrl(photoUrl);
        }

        Family savedFamily = familyRepository.save(family);
        List<Member> members = saveMembersFromFamily(familyRequest, savedFamily.getId());
        return new FamilyResponseDTO(savedFamily, members);
    }

    private List<Member> saveMembersFromFamily(CreateFamilyRequest familyRequest, String familyId) {
        if (familyRequest.getFamilyMembers() == null || familyRequest.getFamilyMembers().isEmpty()) {
            return Collections.emptyList();
        }
        List<Member> members = familyRequest.getFamilyMembers()
                .stream()
                .map(dto -> new Member(dto, familyId, familyRequest.getAddress(), familyRequest.getUnit()))
                .toList();
        memberService.saveAllMembers(members);
        return members;
    }

    /**
     * Adds a new family member to a specified family.
     *
     * @param familyId      the ID of the family to which the member will be added
     * @param memberRequests the request data for the new family member
     * @return the updated FamilyResponseDTO containing the family details
     */
    public void addFamilyMember(String familyId, List<CreateMemberRequest> memberRequests) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found"));
       memberRequests.stream()
               .map(memReq-> new Member(memReq, familyId, family.getAddress(), family.getUnit()))
               .forEach(memberService::saveMember);
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
                .orElse(null);
        if (Objects.isNull(family)) {
            return null;
        }

        List<Member> members = memberService.getMembersByFamilyId(id);
        return new FamilyResponseDTO(family, members);
    }

    /**
     * Lists all families stored in the database.
     *
     * @return a list of FamilyResponseDTO representing all families
     */
    public List<FamilyResponseDTO> listAllFamilies() {
        List<Family> families = familyRepository.findAll();
        return families.stream().map(f -> {
            List<Member> members = memberService.getMembersByFamilyId(f.getId());
            return new FamilyResponseDTO(f, members);
        }).toList();
    }

    /**
     * Updates an existing family's information.
     *
     * @param id            the ID of the family to update
     * @param familyRequest the request data containing updated family details
     * @return the updated FamilyResponseDTO with the family's details
     * @throws Exception if an error occurs during the update process
     */
    @Transactional
    public FamilyResponseDTO updateFamily(String id, UpdateFamilyRequest familyRequest) throws Exception {
        if (!familyRepository.existsById(id)) {
            throw new RuntimeException("Family not found");
        }
        Family updatedFamily = new Family(id, familyRequest);

        // Handle photo upload
        if (familyRequest.getPhoto() != null && !familyRequest.getPhoto().isEmpty()) {
            String fileName = s3Service.uploadImage(familyRequest.getPhoto());
            String photoUrl = s3Service.generatePublicUrl(fileName);
            updatedFamily.setPhotoUrl(photoUrl);
        }
        familyRepository.save(updatedFamily);

        List<Member> members = new ArrayList<>();
        // Update family members if provided
        if (familyRequest.getFamilyMembers() != null && !familyRequest.getFamilyMembers().isEmpty()) {
            members = familyRequest.getFamilyMembers()
                    .stream()
                    .map(dto -> new Member(dto, id, familyRequest.getAddress(), familyRequest.getUnit())).toList();
            memberService.saveAllMembers(members);
        }

        return new FamilyResponseDTO(updatedFamily, members);
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
    public void updateFamilyMember(String familyId, String memberId, CreateMemberRequest memberRequest) {

        Family family = familyRepository.findById(familyId).orElseThrow(() -> new RuntimeException("Family not found"));

        memberService.getMembersByFamilyId(familyId).stream()
                .filter(mem -> mem.getId().equals(memberId))
                .findFirst().orElseThrow(() -> new RuntimeException("Family member not found"));

        Member updatedMember = new Member(memberId, memberRequest, familyId, family.getAddress(), family.getUnit());

        memberService.saveMember(updatedMember);
    }

    /**
     * Deletes a family member from a specified family.
     *
     * @param familyId the ID of the family from which the member will be deleted
     * @param memberId the ID of the member to delete
     * @return the updated FamilyResponseDTO containing the family's details
     * @throws RuntimeException if the family or member is not found
     */
    public void deleteFamilyMember(String familyId, String memberId) {
        if (!familyRepository.existsById(familyId)) {
            throw new RuntimeException("Family not found");
        }

        memberService.getMembersByFamilyId(familyId).stream()
                .filter(member -> member.getId().equals(memberId))
                .findFirst().orElseThrow(() -> new RuntimeException("Family member not found"));

        memberService.deleteMember(memberId);
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

//    /**
//     * Converts a Family object to a FamilyResponseDTO.
//     *
//     * @param family the Family object to convert
//     * @return the converted FamilyResponseDTO
//     */
   /* private FamilyResponseDTO convertToResponseDTO(Family family) {
        FamilyResponseDTO response = new FamilyResponseDTO();
        response.setId(family.getId());
        // response.setName(family.getName());
        response.setAddress(family.getAddress());
        response.setAnniversaryDate(family.getAnniversaryDate());
        response.setPhotoUrl(family.getPhotoUrl());

        List<MemberResponseDTO> memberResponses = family.getFamilyMembers().stream()
                .map(member -> {
                    MemberResponseDTO memberResponse = new MemberResponseDTO();
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
    }*/

    public List<MemberResponseDTO> searchFamilies(SearchRequest searchRequest) {
        Query query = buildSearchQuery(searchRequest);
        List<Member> members = mongoTemplate.find(query, Member.class);
        return mapToResponseDTOs(members);
    }

    public Optional<Member> findFamilyHeadByUserName(String username) {
        SearchRequest searchRequest = buildSearchRequest(username);
        Query query = buildSearchQuery(searchRequest);
        List<Member> members = mongoTemplate.find(query, Member.class);
        return members.stream().findFirst();
    }

    private SearchRequest buildSearchRequest(String username) {
        // Create filter for email ID
        FieldFilter emailFilter = createFieldFilter("emailId", Operation.EQUALS, List.of(username));

        // Create filter to check if the member is the family head
        FieldFilter familyHeadFilter = createFieldFilter("isFamilyHead", Operation.EQUALS, List.of(true));

        // Combine filters with AND evaluation
        FilterCriteria filterCriteria = new FilterCriteria();
        filterCriteria.setEvaluationType(EvaluationType.AND);
        filterCriteria.setFilters(List.of(emailFilter, familyHeadFilter));

        // Build the search request with the combined filter criteria
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setNode(filterCriteria);
        searchRequest.setOffset(1);
        searchRequest.setPageSize(1);

        return searchRequest;
    }

    private FieldFilter createFieldFilter(String fieldName, Operation operation, List<Object> values) {
        // Create and return a field filter with the specified parameters
        FieldFilter fieldFilter = new FieldFilter();
        fieldFilter.setFieldName(fieldName);
        fieldFilter.setOperation(operation);
        fieldFilter.setValues(values);
        return fieldFilter;
    }

    private Query buildSearchQuery(SearchRequest searchRequest) {
        Criteria searchCriteria = CriteriaHelper.createCriteria(searchRequest.getNode());
        return new Query()
                .addCriteria(searchCriteria)
                .limit(searchRequest.getPageSize())
                .skip((long) (searchRequest.getOffset() - 1) * searchRequest.getPageSize());
    }

    private List<MemberResponseDTO> mapToResponseDTOs(List<Member> members) {
        return members.stream()
                .map(MemberResponseDTO::new)
                .toList();
    }

}
