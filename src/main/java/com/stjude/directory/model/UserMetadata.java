package com.stjude.directory.model;

import com.stjude.directory.enums.Role;
import com.stjude.directory.enums.Unit;
import lombok.Data;

import java.util.List;

@Data
public class UserMetadata {
    private String userId;
    private String familyId;
    private List<Role> roles;
    private Unit unit;
    private String emailId;
    private String photoUrl;
    private String familyHeadName;
}
