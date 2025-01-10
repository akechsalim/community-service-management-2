package com.akechsalim.community_service_management_2.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testUnauthenticatedAccess() throws Exception {
        mvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthenticatedAccess() throws Exception {
        // Assuming you have a way to get a valid token for an admin user
        String token = getValidAdminToken();

        mvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private String getValidAdminToken() {
        String secretKey = "yourSecretKeyWithAtLeast32CharactersLong"; // Use the same secret key used in production
        return Jwts.builder()
                .setSubject("Victor Mbogo") // or whatever username you're using for admin
                .claim("roles", List.of("ADMIN")) // or however you store roles in JWT
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour validity
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
}
