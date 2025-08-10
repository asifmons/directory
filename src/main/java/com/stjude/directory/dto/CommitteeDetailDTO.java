package com.stjude.directory.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommitteeDetailDTO {
    private String id;
    private String name;
    private List<CommitteeCardDetailDTO> cards;
}
