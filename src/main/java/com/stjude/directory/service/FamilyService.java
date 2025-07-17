package com.stjude.directory.service;

import com.opencsv.CSVReader;
import com.stjude.directory.CriteriaHelper;
import com.stjude.directory.dto.*;
import com.stjude.directory.dto.MemberRowCSVTemplate; // Added import
import com.stjude.directory.enums.*;
import com.stjude.directory.enums.Unit;
import com.stjude.directory.model.*;
import com.stjude.directory.repository.FamilyRepository;
import com.stjude.directory.utils.AtlasSearchHelper;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

    private final PasswordEncoder passwordEncoder;

    public FamilyService(FamilyRepository familyRepository, MemberService memberService,
                         S3Service s3Service, MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.familyRepository = familyRepository;
        this.memberService = memberService;
        this.s3Service = s3Service;
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
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
     * @param familyId       the ID of the family to which the member will be added
     * @param memberRequests the request data for the new family member
     * @return the updated FamilyResponseDTO containing the family details
     */
    public void addFamilyMember(String familyId, List<CreateMemberRequest> memberRequests) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found"));
        memberRequests.stream()
                .map(memReq -> new Member(memReq, familyId, family.getAddress(), family.getUnit()))
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
     * Updates an existing family member's information using CreateMemberRequest.
     * This method is used by the controller endpoint for individual member updates.
     *
     * @param familyId      the ID of the family to which the member belongs
     * @param memberId      the ID of the member to update
     * @param memberRequest the request data for updating the family member
     * @throws RuntimeException if the family or member is not found
     */
    public void updateFamilyMember(String familyId, String memberId, CreateMemberRequest memberRequest) {
        // Get member using the query method
        Member existingMember = memberService.getMemberByFamilyIdAndMemberId(familyId, memberId);

        // Update only non-null fields from memberRequest
        if (memberRequest.getName() != null) {
            existingMember.setName(memberRequest.getName());
        }
        if (memberRequest.getDob() != null) {
            existingMember.setDob(memberRequest.getDob());
        }
        if (memberRequest.getPhoneNumber() != null) {
            existingMember.setPhoneNumber(memberRequest.getPhoneNumber());
        }
        if (memberRequest.getBloodGroup() != null) {
            existingMember.setBloodGroup(memberRequest.getBloodGroup());
        }
        if (memberRequest.getIsFamilyHead() != null) {
            existingMember.setIsFamilyHead(memberRequest.getIsFamilyHead());
        }
        if (memberRequest.getEmailId() != null) {
            existingMember.setEmailId(memberRequest.getEmailId());
        }
        if (memberRequest.getCoupleNo() != null) {
            existingMember.setCoupleNo(memberRequest.getCoupleNo());
        }
        if (memberRequest.getRoles() != null) {
            existingMember.setRoles(memberRequest.getRoles());
        }

        memberService.saveMember(existingMember);
    }

    /**
     * Updates an existing family member's information using UpdateMemberRequest.
     * This method is used internally by updateFamily method.
     *
     * @param familyId      the ID of the family to which the member belongs
     * @param memberId      the ID of the member to update
     * @param memberRequest the request data for updating the family member
     * @throws RuntimeException if the family or member is not found
     */
    public void updateFamilyMemberWithUpdateRequest(String familyId, String memberId, UpdateMemberRequest memberRequest) {
        // Get member using the query method
        Member existingMember = memberService.getMemberByFamilyIdAndMemberId(familyId, memberId);

        // Update only non-null fields from memberRequest
        if (memberRequest.getName() != null) {
            existingMember.setName(memberRequest.getName());
        }
        if (memberRequest.getDob() != null) {
            existingMember.setDob(memberRequest.getDob());
        }
        if (memberRequest.getPhoneNumber() != null) {
            existingMember.setPhoneNumber(memberRequest.getPhoneNumber());
        }
        if (memberRequest.getBloodGroup() != null) {
            existingMember.setBloodGroup(memberRequest.getBloodGroup());
        }
        if (memberRequest.getIsFamilyHead() != null) {
            existingMember.setIsFamilyHead(memberRequest.getIsFamilyHead());
        }
        if (memberRequest.getEmailId() != null) {
            existingMember.setEmailId(memberRequest.getEmailId());
        }
        if (memberRequest.getCoupleNo() != null) {
            existingMember.setCoupleNo(memberRequest.getCoupleNo());
        }

        memberService.saveMember(existingMember);
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
        // Get existing family
        Family existingFamily = familyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family not found"));

        // Update family fields only if not null
        if (familyRequest.getAddress() != null) {
            existingFamily.setAddress(familyRequest.getAddress());
        }
        if (familyRequest.getUnit() != null) {
            existingFamily.setUnit(familyRequest.getUnit());
        }
        if (familyRequest.getAnniversaryDates() != null) {
            existingFamily.setAnniversaryDates(familyRequest.getAnniversaryDates());
        }
        if (familyRequest.getHouseName() != null) {
            existingFamily.setHouseName(familyRequest.getHouseName());
        }

        // 1. Remove couples
        if (familyRequest.getCouplesToBeRemoved() != null && !familyRequest.getCouplesToBeRemoved().isEmpty()) {
            for (Couple coupleToRemove : familyRequest.getCouplesToBeRemoved()) {
                if (coupleToRemove.getCoupleNo() != null) {
                    existingFamily.getAnniversaryDates().remove(coupleToRemove.getCoupleNo());
                }
            }
        }

        // 2. Update anniversary dates for couples
        if (familyRequest.getCouplesThatNeedUpdate() != null && !familyRequest.getCouplesThatNeedUpdate().isEmpty()) {
            for (Couple coupleToUpdate : familyRequest.getCouplesThatNeedUpdate()) {
                if (coupleToUpdate.getCoupleNo() != null && coupleToUpdate.getAnniversaryDate() != null) {
                    existingFamily.getAnniversaryDates().put(coupleToUpdate.getCoupleNo(), coupleToUpdate.getAnniversaryDate());
                }
            }
        }

        // Save updated family
        Family updatedFamily = familyRepository.save(existingFamily);

        List<Member> members = new ArrayList<>();
        // Update family members if provided
        if (familyRequest.getFamilyMembers() != null && !familyRequest.getFamilyMembers().isEmpty()) {
            for (UpdateMemberRequest memberRequest : familyRequest.getFamilyMembers()) {
                // Use the new update method for UpdateMemberRequest
                updateFamilyMemberWithUpdateRequest(id, memberRequest.getId(), memberRequest);
            }
            members = memberService.getMembersByFamilyId(id);
        } else {
            // If no members provided in request, get existing members
            members = memberService.getMembersByFamilyId(id);
        }

        // 3. Remove family members
        if (familyRequest.getFamilyMembersToRemove() != null && !familyRequest.getFamilyMembersToRemove().isEmpty()) {
            for (String memberIdToRemove : familyRequest.getFamilyMembersToRemove()) {
                memberService.deleteMember(memberIdToRemove);
            }
        }

        // 4. Handle members to be added
        if (familyRequest.getFamilyMembersToAdd() != null && !familyRequest.getFamilyMembersToAdd().isEmpty()) {
            for (AddMemberRequest memberRequest : familyRequest.getFamilyMembersToAdd()) {
                Member newMember = new Member(memberRequest, updatedFamily.getId(), updatedFamily.getAddress(), updatedFamily.getUnit());

                if (memberRequest.getAnniversaryDate() != null && memberRequest.getPartnerId() != null) {
                    Short coupleNo = getNextCoupleNo(existingFamily.getAnniversaryDates());
                    newMember.setCoupleNo(coupleNo);
                    existingFamily.getAnniversaryDates().put(coupleNo, memberRequest.getAnniversaryDate());

                    // Update partner's coupleNo
                    Member partner = memberService.getMemberById(memberRequest.getPartnerId());
                    if (partner != null) {
                        partner.setCoupleNo(coupleNo);
                        newMember.setCoupleNo(coupleNo);
                        memberService.saveMember(partner);
                        Map<Short, Date> anniversaryDates = updatedFamily.getAnniversaryDates();
                        if (anniversaryDates == null) {
                            anniversaryDates = new HashMap<>();
                        }
                        anniversaryDates.put(newMember.getCoupleNo(), memberRequest.getAnniversaryDate());
                        updatedFamily.setAnniversaryDates(anniversaryDates);
                        updatedFamily = familyRepository.save(updatedFamily);
                        members.add(newMember);
                    }
                }
                memberService.saveMember(newMember);
            }
        }



        return new FamilyResponseDTO(updatedFamily, members);
    }

    private Short getNextCoupleNo(Map<Short, Date> anniversaryDates) {
        if (anniversaryDates == null || anniversaryDates.isEmpty()) {
            return 1;
        }
        Optional<Short> maxCoupleNo = anniversaryDates.keySet().stream()
                .max(Comparator.naturalOrder());
        return (short) (maxCoupleNo.orElse((short) 0) + 1);
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

        // Delete all members associated with the family
        List<Member> members = memberService.getMembersByFamilyId(id);
        if (members != null && !members.isEmpty()) {
            members.forEach(member -> memberService.deleteMember(member.getId()));
        }


        // Delete the family from MongoDB
        familyRepository.deleteById(id);
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
     * Extracts the S3 key from the provided S3 URL.
     *
     * @param url the S3 URL
     * @return the extracted key
     */
    private String extractS3KeyFromUrl(String url) {
        int index = url.indexOf(".amazonaws.com/");
        if (index == -1) {
            throw new IllegalArgumentException("Not a valid S3 URL: " + url);
        }
        return url.substring(index + ".amazonaws.com/".length());
    }


    public List<MemberResponseDTO> searchFamilies(SearchRequest searchRequest) {
        List<Member> members =  searchMembersUsingAtlasSearch(searchRequest);
        return mapToResponseDTOs(members);
    }

    public SearchResultWithCountDTO searchFamiliesWithCount(SearchRequest searchRequest) {
        // Get the search results
        List<Member> members = searchMembersUsingAtlasSearch(searchRequest);
        List<MemberResponseDTO> results = mapToResponseDTOs(members);
        
        // Get the total count efficiently using a separate aggregation
        long totalCount = getSearchResultCount(searchRequest);
        
        return new SearchResultWithCountDTO(results, totalCount);
    }

    private long getSearchResultCount(SearchRequest searchRequest) {
        Document searchQuery = AtlasSearchHelper.createSearchQuery(searchRequest.getNode());

        // Build aggregation pipeline for counting
        List<AggregationOperation> operations = new ArrayList<>();

        // Add search stage
        operations.add(context -> searchQuery);
        
        // Add count stage
        operations.add(Aggregation.count().as("totalCount"));
        
        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation,
                "MEMBER", // Collection name
                Document.class
        );

        List<Document> mappedResults = results.getMappedResults();
        if (mappedResults.isEmpty()) {
            return 0L;
        }
        
        return mappedResults.get(0).getInteger("totalCount", 0);
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

    private List<Member> searchMembersUsingAtlasSearch(SearchRequest searchRequest) {
        Document searchQuery = AtlasSearchHelper.createSearchQuery(searchRequest.getNode());

        // Build aggregation pipeline manually
        List<AggregationOperation> operations = new ArrayList<>();

        // Add search stage
        operations.add(context -> searchQuery);
        
        // Add a stage to include the search score in the results
        operations.add(context -> new Document("$addFields", 
            new Document("searchScore", new Document("$meta", "searchScore"))));
        
        // Sort first by highest matching score (descending), then by name (ascending)
        operations.add(Aggregation.sort(
            Sort.by(Sort.Direction.DESC, "searchScore")
                .and(Sort.by(Sort.Direction.ASC, "name"))
        ));

        // Add skip stage if specified
        operations.add(Aggregation.skip((long) (searchRequest.getOffset() - 1) * searchRequest.getPageSize()));

        // Add limit stage if specified
        operations.add(Aggregation.limit(searchRequest.getPageSize()));
        
        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<Member> results = mongoTemplate.aggregate(
                aggregation,
                "MEMBER", // Collection name
                Member.class
        );

        return results.getMappedResults();
    }

    private List<MemberResponseDTO> mapToResponseDTOs(List<Member> members) {
        return members.stream()
                .map(MemberResponseDTO::new)
                .toList();
    }

    public String uploadPhoto(String familyId, MultipartFile file) throws IOException {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found with given id."));

        if (file == null || file.isEmpty()) {
            return "Photo is null or empty.";
        }


        String fileName = s3Service.uploadImage(file);
        String photoUrl = s3Service.generatePublicUrl(fileName);
        family.setPhotoUrl(photoUrl);
        familyRepository.save(family);
        return photoUrl;
    }

    public String deletePhoto(String familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found with given id."));

        // Optionally, delete the photo from S3
        if (family.getPhotoUrl() == null || family.getPhotoUrl().isEmpty()) {
            return "Photo not present for family.";
        }
        String key = extractS3KeyFromUrl(family.getPhotoUrl());
        s3Service.deleteFileFromS3(key);
        family.setPhotoUrl(null);
        familyRepository.save(family);
        return "Photo deleted successfully";
    }


    public void uploadFamilyData(MultipartFile file) throws Exception {
        validateFile(file); // Step 1: Validate the file
        List<String[]> csvRows = parseCSV(file); // Step 2: Parse the CSV
        Map<String, List<MemberRowCSVTemplate>> familyGroup = groupMembersByFamilyId(csvRows);
        saveFamilies(familyGroup); // Step 4: Save to database
    }

   public Map<String, List<MemberRowCSVTemplate>> groupMembersByFamilyId(List<String[]> csvRows) {
        Map<String, List<MemberRowCSVTemplate>> grouped = new LinkedHashMap<>();
        String currentFamilyId = null;

        for (String[] row : csvRows) {
            if (row.length == 0) continue;
            String familyId = (row[0] != null && !row[0].trim().isEmpty()) ? row[0].trim() : null;

            if (familyId != null) {
                currentFamilyId = familyId;
                grouped.putIfAbsent(currentFamilyId, new ArrayList<>());
            } else if (currentFamilyId == null) {
                // Skip rows without a familyId if no group has started
                continue;
            }

            try {
                MemberRowCSVTemplate memberRow = mapToFamilyEntities(row);
                if (!grouped.get(currentFamilyId).isEmpty()) {
                    memberRow.setAddress(grouped.get(currentFamilyId).get(0).getAddress());
                }
                grouped.get(currentFamilyId).add(memberRow);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Error parsing row: " + Arrays.toString(row), e);
            }
        }
        return grouped;
    }

    // Step 1: Validate the uploaded file
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("The uploaded file is empty.");
        }
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().endsWith(".csv")) {
            throw new IllegalArgumentException("Only CSV files are allowed.");
        }
    }

    // Step 2: Parse the CSV file into rows
    private List<String[]> parseCSV(MultipartFile file) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> rows = new ArrayList<>();
            String[] nextLine;
            // Skip the header line
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                rows.add(nextLine);
            }
            if (rows.isEmpty()) {
                throw new IllegalArgumentException("The CSV file is empty or only contains headers.");
            }
            return rows;
        }
    }

    // Step 3: Map each row to a Family entity
    private MemberRowCSVTemplate mapToFamilyEntities(String[] row) throws ParseException {
        MemberRowCSVTemplate.MemberRowCSVTemplateBuilder builder = MemberRowCSVTemplate.builder();
        
        // Set basic fields that don't require parsing
        builder.familyId(row[0])
               .address(row[1])
               .houseName(row[2])
               .memberName(row[3])
               .relation(row[4])
               .phoneNumber(row[6])
               .isFamilyHead(Boolean.parseBoolean(row[8]))
               .emailId(row[9])
               .salutation(row[12])
               .password(Boolean.parseBoolean(row[8]) ? passwordEncoder.encode("test123") : null);
        
        // Parse date of birth with error handling
        if (row[5] != null && !row[5].isEmpty()) {
            try {
                builder.dob(new SimpleDateFormat("dd.MM.yyyy").parse(row[5].trim()));
            } catch (ParseException e) {
                System.out.println("Error parsing DOB for row: " + Arrays.toString(row) + " - " + e.getMessage());
                builder.dob(null);
            }
        } else {
            builder.dob(null);
        }
        
        // Parse blood group with error handling
        try {
            builder.bloodGroup(BloodGroup.getNameForDisplayValue(row[7]));
        } catch (Exception e) {
            System.out.println("Error parsing BloodGroup for row: " + Arrays.toString(row) + " - " + e.getMessage());
            builder.bloodGroup(null);
        }
        
        // Parse unit with error handling
        try {
            builder.unit(Unit.getByDisplayValue(row[10]));
        } catch (Exception e) {
            System.out.println("Error parsing Unit for row: " + Arrays.toString(row) + " - " + e.getMessage());
            builder.unit(null);
        }
        
        // Parse anniversary date with error handling
        if (row[11] != null && !row[11].isEmpty() && !"NA".equals(row[11])) {
            try {
                builder.anniversaryDate(new SimpleDateFormat("dd.MM.yyyy").parse(row[11].trim()));
            } catch (ParseException e) {
                System.out.println("Error parsing Anniversary Date for row: " + Arrays.toString(row) + " - " + e.getMessage());
                builder.anniversaryDate(null);
            }
        } else {
            builder.anniversaryDate(null);
        }
        
        // Parse status with error handling
        try {
            if (row[13] == null || row[13].isEmpty()) {
                builder.status(Status.ACTIVE);
            } else {
                builder.status(Status.getByDisplayValue(row[13]));
            }
        } catch (Exception e) {
            System.out.println("Error parsing Status for row: " + Arrays.toString(row) + " - " + e.getMessage());
            builder.status(Status.ACTIVE);
        }
        
        // Parse expiry date with error handling
        if (row[14] != null && !row[14].isEmpty()) {
            try {
                builder.expiryDate(new SimpleDateFormat("dd.MM.yyyy").parse(row[14].trim()));
            } catch (ParseException e) {
                System.out.println("Error parsing Expiry Date for row: " + Arrays.toString(row) + " - " + e.getMessage());
                builder.expiryDate(null);
            }
        } else {
            builder.expiryDate(null);
        }
        
        MemberRowCSVTemplate member = builder.build();
        LocalDate localDate = null;

        Instant utcInstant = null;
        if (member.getDob() != null) {
            localDate = member.getDob().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            utcInstant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            member.setDob(Date.from(utcInstant));
        }

        if (member.getExpiryDate() != null) {
             localDate = member.getExpiryDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
             utcInstant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            member.setExpiryDate(Date.from(utcInstant));
        }
        if (member.getAnniversaryDate() != null) {
            localDate = member.getAnniversaryDate().toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDate();
            utcInstant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
           member.setAnniversaryDate(Date.from(utcInstant));
       }
        return member;
    }

    // Step 4: Save the list of Family entities to the database
    private void saveFamilies(Map<String, List<MemberRowCSVTemplate>> familyGroup) {

        familyGroup.entrySet().forEach(

                entry -> {
                    Family family = new Family(entry.getValue().get(0));
                    Map<Short, Date> anniversaryDates = groupAnniversaryDates(entry.getValue());
                    family.setAnniversaryDates(anniversaryDates);

                    List<Member> members = entry.getValue().stream().map(m -> new Member(m, family.getId())).toList();

                    familyRepository.save(family);
                    memberService.saveAllMembers(members);



                    //todo - set coupleno for members
                }

        );
    }

    public Map<Short, Date> groupAnniversaryDates(List<MemberRowCSVTemplate> members) {
        // Map to store unique Short keys for each anniversary date
        Map<Date, Short> dateToKeyMap = new HashMap<>();
        Map<Short, Date> result = new LinkedHashMap<>();
        short coupleNumber = 1;

        // Group members by unique anniversary dates
        for (MemberRowCSVTemplate member : members) {
            Date anniversaryDate = member.getAnniversaryDate();
            if (anniversaryDate != null) {
                // Assign a unique Short key if the date is not already mapped
                dateToKeyMap.putIfAbsent(anniversaryDate, coupleNumber++);
                member.setCoupleNo(dateToKeyMap.get(anniversaryDate));
                // Populate the result map with the Short key and Date
                result.put(dateToKeyMap.get(anniversaryDate), anniversaryDate);
            }
        }

        return result;
    }

    public Family findByAthmasthithiNo(String athamsthithiNumber) {
    if (athamsthithiNumber == null || athamsthithiNumber.isEmpty()) {
        throw new IllegalArgumentException("Athmasthithi number cannot be null or empty.");
    }
        return familyRepository.findByAathmaSthithiNumber(athamsthithiNumber)
                .orElseThrow(() -> new RuntimeException("Family not found with Athmasthithi number: " + athamsthithiNumber));
    }
}
