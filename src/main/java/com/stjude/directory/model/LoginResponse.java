package com.stjude.directory.model;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenTTL;
    private Long refreshTokenTTL;
}
