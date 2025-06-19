package com.stjude.directory.model;

import com.stjude.directory.utils.StringOps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "TERMS_ACCEPTANCE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermsAcceptance {
    
    @Id
    private String id;
    private String familyHeadId;
    private String familyId;
    private String familyHeadName;
    private Date acceptedAt;
    
    public TermsAcceptance(String familyHeadId, String familyId, String familyHeadName) {
        this.id = StringOps.generateUUID();
        this.familyHeadId = familyHeadId;
        this.familyId = familyId;
        this.familyHeadName = familyHeadName;
        this.acceptedAt = new Date();
    }
}
