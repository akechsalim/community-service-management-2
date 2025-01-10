package com.akechsalim.community_service_management_2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenManagerTest {

    private final JwtTokenManager jwtTokenManager = new JwtTokenManager(); // Assuming constructor injection not needed for tests

    @Test
    void testGenerateToken() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("testUser", "password", List.of());
        String token = jwtTokenManager.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("testUser", "password", List.of());
        String token = jwtTokenManager.generateToken(userDetails);
        String username = jwtTokenManager.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testValidateToken() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("testUser", "password", List.of());
        String token = jwtTokenManager.generateToken(userDetails);
        assertTrue(jwtTokenManager.validateToken(token, userDetails));
    }

    @Test
    void testIsTokenExpired() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("testUser", "password", List.of());
        String token = jwtTokenManager.generateToken(userDetails);
        String secretKey = "yourSecretKeyWithAtLeast32CharactersLong"; // Use the same secret key used in production

        assertFalse(jwtTokenManager.isTokenExpired(token)); // Assuming token validity is set for future

        // Manually setting expiration to past for testing expired token
        String expiredToken = Jwts.builder()
                .claim("sub", userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() - 10000)) // Set expiration to past
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
        assertTrue(jwtTokenManager.isTokenExpired(expiredToken));
    }

    // Add more tests for other methods like extractExpiration if needed
}
