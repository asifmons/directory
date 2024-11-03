package com.stjude.directory.model;

import com.stjude.directory.dto.CreateFamilyRequest;
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
@Document(collection = "families")
@NoArgsConstructor
public class Family {

    @Id
    private String id;
    private String address;
    private String anniversaryDate; // Consider using LocalDate for date handling
    private String photoUrl;
    private Unit unit;
    private Map<Short, Date> anniversaryDates;
    private String houseName;

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

}


