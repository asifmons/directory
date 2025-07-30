package com.stjude.directory.dto;

import com.stjude.directory.model.RepresentativeCard;
import java.util.List;
import lombok.Data;

@Data
public class UnitRepresentativesTemplateDTO {
    private String id;
    private String name;
    private List<RepresentativeCard> cards;
}
