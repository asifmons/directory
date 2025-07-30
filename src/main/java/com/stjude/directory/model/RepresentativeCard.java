package com.stjude.directory.model;

import java.util.List;
import lombok.Data;

@Data
public class RepresentativeCard {

  private String id;
  private String title;
  private List<PositionReference> positions;
}
