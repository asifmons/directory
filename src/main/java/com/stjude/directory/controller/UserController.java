package com.stjude.directory.controller;

import com.stjude.directory.dto.UserCreateRequest;
import com.stjude.directory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<String> addNewUser(@RequestBody UserCreateRequest userCreateRequest) {
        userService.createNewUser(userCreateRequest);
        return ResponseEntity.ok("User successfully added");
    }
}
