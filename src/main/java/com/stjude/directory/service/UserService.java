package com.stjude.directory.service;

import com.stjude.directory.dto.CreateFamilyRequest;
import com.stjude.directory.dto.CreateMemberRequest;
import com.stjude.directory.dto.UserCreateRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final FamilyService familyService;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, FamilyService familyService) {
        this.passwordEncoder = passwordEncoder;
        this.familyService = familyService;
    }

    public void createNewUser(UserCreateRequest userCreateRequest) {
        try {
            CreateFamilyRequest familyRequest = mapUserCreateRequestToFamilyRequest(userCreateRequest);
            familyService.createFamily(familyRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new user", e);
        }
    }

    private CreateFamilyRequest mapUserCreateRequestToFamilyRequest(UserCreateRequest userCreateRequest) {
        CreateFamilyRequest createFamilyRequest = new CreateFamilyRequest();
        createFamilyRequest.setUnit(userCreateRequest.getUnit());

        CreateMemberRequest memberRequest = new CreateMemberRequest();
        memberRequest.setName(userCreateRequest.getName());
        memberRequest.setEmailId(userCreateRequest.getUsername());
        memberRequest.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        memberRequest.setRoles(userCreateRequest.getRoles());
        memberRequest.setIsFamilyHead(true);

        createFamilyRequest.setFamilyMembers(List.of(memberRequest));
        return createFamilyRequest;
    }
}