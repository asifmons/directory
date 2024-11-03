package com.stjude.directory.dto;

import com.stjude.directory.enums.Role;
import com.stjude.directory.enums.Unit;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.List;

@Data
public class UserCreateRequest {
    private String name;
    @Email
    private String username;
    private String password;
    private Unit unit;
    private List<Role> roles;
}
