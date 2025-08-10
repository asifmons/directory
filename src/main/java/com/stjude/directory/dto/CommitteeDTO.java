package com.stjude.directory.dto;

import java.util.List;
import lombok.Data;

@Data
public class CommitteeDTO {
    private String name;
    private List<CommitteeCardDTO> cards;
}
