package com.stjude.directory.configuration;

import com.stjude.directory.model.Member;
import com.stjude.directory.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private FamilyService familyService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = familyService.findFamilyHeadByUserName(username);
        return member.map(CustomMemberDetails::new).orElseThrow(() ->
                new UsernameNotFoundException("user not found with name :" + username));
    }
}
