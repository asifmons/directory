package com.stjude.directory.dto;

import java.util.List;

public class SearchResultWithCountDTO {
    private List<MemberResponseDTO> results;
    private long totalCount;

    public SearchResultWithCountDTO() {}

    public SearchResultWithCountDTO(List<MemberResponseDTO> results, long totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }

    public List<MemberResponseDTO> getResults() {
        return results;
    }

    public void setResults(List<MemberResponseDTO> results) {
        this.results = results;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
