package com.stjude.directory.filter;

import com.stjude.directory.model.UserContext;
import com.stjude.directory.model.UserContextHolder;
import com.stjude.directory.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtTokenService;

    public JwtTokenFilter(JwtService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT token from Authorization header
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Strip "Bearer " prefix

            String userId = jwtTokenService.extractUserIdFromToken(token);

            // Create UserContext and set the userId
            if (userId != null) {
                UserContext userContext = new UserContext();
                userContext.setUserId(userId);


                // Set UserContext in the current thread
                UserContextHolder.setUserContext(userContext);
            }
        }

        // Proceed with the next filter
        filterChain.doFilter(request, response);
    }
}
