package com.stjude.directory.service;

import com.stjude.directory.model.RefreshToken;
import com.stjude.directory.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RefreshTokenService implements TokenService<String> {

    static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60; // 1 hour

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public String createToken(String emailId) {
        RefreshToken refreshToken = buildRefreshToken(emailId);
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        return savedToken.getId();
    }

    private RefreshToken buildRefreshToken(String emailId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmailId(emailId);
        refreshToken.setExpiryTime(calculateExpiryTime());
        return refreshToken;
    }

    private Instant calculateExpiryTime() {
        return Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY);
    }

    @Override
    public Boolean isTokenValid() {
        // Implement logic for token validation (e.g., check expiry date)
        return null;
    }
}
