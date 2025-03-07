package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.UserLoginDTO;
import com.akechsalim.community_service_management_2.dto.UserRegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.akechsalim.community_service_management_2.model.Role.VOLUNTEER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws Exception {
        // Register a test user before login tests
        UserRegisterDTO registerDTO = new UserRegisterDTO("testuser", "test@example.com", "Password123!", VOLUNTEER);
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)));
        // Note: This assumes OTP verification is bypassed or mocked in tests
    }

//    @Test
//    public void testRegisterUser() throws Exception {
//        UserRegisterDTO dto = new UserRegisterDTO("newuser", "newuser@example.com", "Password123!", VOLUNTEER);
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value("User registered successfully. Please check your email for OTP."))
//                .andExpect(jsonPath("$.otpRequired").value(true));
//    }

//    @Test
//    public void testLoginUser() throws Exception {
//        UserLoginDTO loginDTO = new UserLoginDTO("testuser", "Password123!");
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.role").value("VOLUNTEER"))
//                .andExpect(jsonPath("$.token").exists()); // Check for token presence
//    }

//    @Test
//    public void testLoginWithInvalidCredentials() throws Exception {
//        UserLoginDTO loginDTO = new UserLoginDTO("testuser", "wrongPassword");
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginDTO)))
//                .andExpect(status().isUnauthorized()); // 401 for invalid credentials
//    }
}