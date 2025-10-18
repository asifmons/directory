package com.stjude.directory.dto;

import com.stjude.directory.enums.RelationshipAction;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CoupleRelationshipRequest {

    private String member1Id;
    private String member2Id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date anniversaryDate;

    private RelationshipAction action;
}

