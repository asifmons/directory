package com.stjude.directory.service;

import com.stjude.directory.dto.MemberResponseDTO;
import com.stjude.directory.enums.EvaluationType;
import com.stjude.directory.enums.Operation;
import com.stjude.directory.model.FieldFilter;
import com.stjude.directory.model.FilterCriteria;
import com.stjude.directory.model.SearchRequest;
import com.stjude.directory.model.UserMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.stjude.directory.service.AuthService.DEFAULT_OFFSET;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Component
public class UserService {
    @Autowired
    private FamilyService familyService;
    public ResponseEntity<UserMetadata> getUserMetaData(String userEmail) {
        List<MemberResponseDTO> members = familyService.searchFamilies(createSearchRequest(userEmail));
        MemberResponseDTO member = members.getFirst();
        return mapDataToMetaData(member);
    }

    private ResponseEntity<UserMetadata> mapDataToMetaData(MemberResponseDTO member) {
        UserMetadata userMetadata = new UserMetadata();
        userMetadata.setUserId(member.getId());
        userMetadata.setUnit(member.getUnit());
        userMetadata.setFamilyId(member.getFamilyId());
        userMetadata.setRoles(member.getRoles());
        userMetadata.setEmailId(member.getEmailId());
        return ResponseEntity.ok(userMetadata);
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
