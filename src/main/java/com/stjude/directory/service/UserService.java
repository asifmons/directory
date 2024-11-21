package com.stjude.directory.service;

import com.stjude.directory.dto.CreateFamilyRequest;
import com.stjude.directory.dto.CreateMemberRequest;
import com.stjude.directory.dto.UserCreateRequest;
import com.stjude.directory.enums.EvaluationType;
import com.stjude.directory.enums.Operation;
import com.stjude.directory.model.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.stjude.directory.service.JwtService.JWT_TOKEN_VALIDITY;
import static com.stjude.directory.service.RefreshTokenService.REFRESH_TOKEN_VALIDITY;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final FamilyService familyService;

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private static final int DEFAULT_PAGE_SIZE = 1;
    private static final int DEFAULT_OFFSET = 1;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, FamilyService familyService,
                       JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.familyService = familyService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
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

    public LoginResponse generateToken(String emailId) {
        String jwtToken = jwtService.createToken(emailId);
        String refreshToken = refreshTokenService.createToken(emailId);

        return createLoginResponse(jwtToken, refreshToken);
    }

    private LoginResponse createLoginResponse(String jwtToken, String refreshToken) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(jwtToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setAccessTokenTTL(JWT_TOKEN_VALIDITY);
        loginResponse.setRefreshTokenTTL(REFRESH_TOKEN_VALIDITY);
        return loginResponse;
    }

    private SearchRequest createSearchRequest(String emailId) {
        FilterCriteria filterCriteria = createFilterCriteria(emailId);
        return buildSearchRequest(filterCriteria);
    }

    private FilterCriteria createFilterCriteria(String emailId) {
        FieldFilter fieldFilter = new FieldFilter();
        fieldFilter.setFieldName("emailId");
        fieldFilter.setOperation(Operation.EQUALS);
        fieldFilter.setValues(List.of(emailId));

        FilterCriteria filterCriteria = new FilterCriteria();
        filterCriteria.setEvaluationType(EvaluationType.AND);
        filterCriteria.setFilters(List.of(fieldFilter));

        return filterCriteria;
    }

    private SearchRequest buildSearchRequest(FilterCriteria filterCriteria) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setPageSize(DEFAULT_PAGE_SIZE);
        searchRequest.setOffset(DEFAULT_OFFSET);
        searchRequest.setNode(filterCriteria);
        return searchRequest;
    }
}
