package com.stjude.directory.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService implements TokenService<String> {

    static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 10; // 10 minutes

    @Value("${app.secret}")
    private String secret;

    @Override
    public String createToken(String emailId) {
        Map<String, Object> claims = Map.of("emailId", emailId);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(generateExpirationDate())
                .signWith(getSignKey())
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Boolean isTokenValid() {
        // Implementation for token validation should go here
        return null;
    }

    public String extractUserIdFromToken(String token) {
        Claims claims;
        try {
            claims = parseClaims(token);
        } catch (ExpiredJwtException | SignatureException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        return claims.get("emailId").toString();
    }

    /**
     * Parses the claims from the provided JWT token.
     *
     * @param token the JWT token to be parsed.
     * @return the claims extracted from the token.
     */
    Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).build()
                .parseClaimsJws(token).getBody();
    }
}
