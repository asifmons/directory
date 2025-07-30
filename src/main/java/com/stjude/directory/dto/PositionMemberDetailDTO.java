package com.stjude.directory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionMemberDetailDTO {
    private String positionName;
    private String positionId;
    private String memberId;
    private String memberName;
}
