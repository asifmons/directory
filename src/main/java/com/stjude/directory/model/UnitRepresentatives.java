package com.stjude.directory.model;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("unit_representatives")
@Data
public class UnitRepresentatives {

  @Id
  private String id;
  private String unitId;
  private String templateId;
  private Map<String, Map<String, String>> cardPositionMemberMap; // Map of card title to positionId to memberId
  private String coverPhotoUrl;
  private String innerCoverPhotoUrl;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime lastModifiedDate;
}
