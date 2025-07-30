package com.stjude.directory.dto;

import com.stjude.directory.model.Member;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnitRepresentativesResponseDTO {

  private String title;
  private List<Map<String, Member>> members;
}
