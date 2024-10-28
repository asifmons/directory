package com.stjude.directory.model;

import com.stjude.directory.dto.CreateFamilyRequest;
import com.stjude.directory.enums.Unit;
import com.stjude.directory.utils.StringOps;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "families")
@NoArgsConstructor
public class Family {

    @Id
    private String id;
    private String address;
    private String anniversaryDate; // Consider using LocalDate for date handling
    private List<FamilyMember> familyMembers;
    private String photoUrl;
    private Unit unit;
    private List<Couple> couples;
    private String houseName;

    public Family(CreateFamilyRequest request){
        this.id = StringOps.generateUUID();
        this.address = request.getAddress();
        this.unit = request.getUnit();
        this.couples = request.getCouples();
        this.houseName = request.getHouseName();
        //this.anniversaryDate = request.getAnniversaryDate();
    }

}


