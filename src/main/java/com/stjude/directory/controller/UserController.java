package com.stjude.directory.controller;

import com.stjude.directory.annotation.CurrentUser;
import com.stjude.directory.model.UserMetadata;
import com.stjude.directory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path = "user-metadata")
    public ResponseEntity<UserMetadata> getUserMetadata(@CurrentUser String userEmail) {
        return userService.getUserMetaData(userEmail);
    }
}
