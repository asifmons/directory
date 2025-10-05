package com.stjude.directory.model;

import java.time.LocalDate;
import java.util.List;

public class AnniversaryEvent extends Event {

    private String familyId;
    private List<CoupleMember> couple;

    public AnniversaryEvent(String name, LocalDate date, String familyId, List<CoupleMember> couple) {
        super(name, date, EventType.ANNIVERSARY);
        this.familyId = familyId;
        this.couple = couple;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public List<CoupleMember> getCouple() {
        return couple;
    }

    public void setCouple(List<CoupleMember> couple) {
        this.couple = couple;
    }
}
