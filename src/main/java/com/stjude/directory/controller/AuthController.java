package com.stjude.directory.controller;

import com.stjude.directory.dto.UserCreateRequest;
import com.stjude.directory.model.LoginRequest;
import com.stjude.directory.model.LoginResponse;
import com.stjude.directory.service.AuthService;
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
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping
    public ResponseEntity<String> addNewUser(@RequestBody UserCreateRequest userCreateRequest) {
        authService.createNewUser(userCreateRequest);
        return ResponseEntity.ok("User successfully added");
    }
    @PostMapping("token")
    public ResponseEntity<LoginResponse> getToken(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(loginRequest.emailId(),
                        loginRequest.password()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.ok(authService.generateToken(loginRequest.emailId()));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Invalid User Credentials provided");
    }

    @PostMapping("reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody LoginRequest loginRequest) {
         authService.resetPassword(loginRequest);
        return ResponseEntity.ok("Password reset successfully");
    }

}
