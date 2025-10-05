package com.stjude.directory.model;

import lombok.Data;

@Data
public class CoupleMember {
    private String memberId;
    private String name;

    public CoupleMember(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }
}
