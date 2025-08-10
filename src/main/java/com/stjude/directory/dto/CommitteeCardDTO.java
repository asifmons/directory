package com.stjude.directory.dto;

import java.util.List;
import lombok.Data;

@Data
public class CommitteeCardDTO {
    private String title;
    private List<CommitteePositionDTO> positions;
}
