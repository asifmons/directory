package com.stjude.directory.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetailDTO {
    private String title;
    private List<PositionMemberDetailDTO> positions;
}
