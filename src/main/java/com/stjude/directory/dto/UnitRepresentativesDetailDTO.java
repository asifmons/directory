package com.stjude.directory.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnitRepresentativesDetailDTO {
    private String coverPhotoUrl;
    private String innerCoverPhotoUrl;
    private List<CardDetailDTO> cards;
}
