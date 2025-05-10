package com.stjude.directory.model;

import com.stjude.directory.dto.CreateFamilyRequest;
import com.stjude.directory.dto.MemberRowCSVTemplate;
import com.stjude.directory.dto.UpdateFamilyRequest;
import com.stjude.directory.enums.Unit;
import com.stjude.directory.utils.StringOps;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "FAMILY")
@NoArgsConstructor
public class Family {

    @Id
    private String id;
    private String churchNo;
    private String address;
    //private String anniversaryDate; // Consider using LocalDate for date handling
    private String photoUrl;
    private Unit unit;
    private Map<Short, Date> anniversaryDates;
    private String houseName;
    private String aathmaSthithiNumber;

    public Family(CreateFamilyRequest request) {
        this.id = StringOps.generateUUID();
        this.address = request.getAddress();
        this.unit = request.getUnit();
        this.anniversaryDates = request.getAnniversaryDates();
        this.houseName = request.getHouseName();
    }

    public Family(String id, UpdateFamilyRequest request) {
        this.id = id;
        this.address = request.getAddress();
        this.unit = request.getUnit();
        this.anniversaryDates = request.getAnniversaryDates();
        this.houseName = request.getHouseName();
    }

    public Family(UpdateFamilyRequest request) {
        this.address = request.getAddress();
        this.unit = request.getUnit();
        this.anniversaryDates = request.getAnniversaryDates();
        this.houseName = request.getHouseName();
    }

    public Family(MemberRowCSVTemplate template) {
        this.id = StringOps.generateUUID();
        this.address = template.getAddress();
        this.unit = template.getUnit();
        this.houseName = template.getHouseName();
        this.aathmaSthithiNumber = template.getFamilyId();
    }

}


