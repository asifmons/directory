package com.stjude.directory.controller;

import com.stjude.directory.dto.UserCreateRequest;
import com.stjude.directory.model.LoginRequest;
import com.stjude.directory.model.LoginResponse;
import com.stjude.directory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping
    public ResponseEntity<String> addNewUser(@RequestBody UserCreateRequest userCreateRequest) {
        userService.createNewUser(userCreateRequest);
        return ResponseEntity.ok("User successfully added");
    }
    @PostMapping("token")
    public ResponseEntity<LoginResponse> getToken(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(loginRequest.emailId(),
                        loginRequest.password()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.ok(userService.generateToken(loginRequest.emailId()));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Invalid User Credentials provided");
    }
}
