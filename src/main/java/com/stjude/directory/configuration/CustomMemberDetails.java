package com.stjude.directory.configuration;

import com.stjude.directory.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomMemberDetails implements UserDetails {

    private final String username;
    private final String password;

    public CustomMemberDetails(Member member) {
        this.username = member.getEmailId();
        this.password = member.getPassword();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
