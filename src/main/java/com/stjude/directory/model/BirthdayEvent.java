package com.stjude.directory.model;

import java.time.LocalDate;

public class BirthdayEvent extends Event {

    private String memberId;
    private String familyId;

    public BirthdayEvent(String name, LocalDate date, String memberId, String familyId) {
        super(name, date, EventType.BIRTHDAY);
        this.memberId = memberId;
        this.familyId = familyId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
}
