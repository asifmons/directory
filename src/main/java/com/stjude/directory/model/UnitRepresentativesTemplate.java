package com.stjude.directory.model;

import java.util.List;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("unit_representatives_templates")
@Data
public class UnitRepresentativesTemplate {

  @Id
  private String id = UUID.randomUUID().toString();
  private String name;
  private List<RepresentativeCard> cards;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime lastModifiedDate;
}
