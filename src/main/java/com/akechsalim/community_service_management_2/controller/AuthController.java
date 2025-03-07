package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.UserLoginDTO;
import com.akechsalim.community_service_management_2.dto.UserRegisterDTO;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.security.JwtTokenManager;
import com.akechsalim.community_service_management_2.service.OtpService;
import com.akechsalim.community_service_management_2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final JwtTokenManager jwtTokenManager;

    public AuthController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
        this.jwtTokenManager = new JwtTokenManager();
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        System.out.println("Received DTO: " + userRegisterDTO); // Debug log
        userService.createUser(userRegisterDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully. Please check your email for OTP.");
        response.put("otpRequired", true);
        return ResponseEntity.status(201).body(response);
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> otpRequest) {
        String email = otpRequest.get("email");
        String otp = otpRequest.get("otp");

        if (otpService.verifyOtp(email, otp)) {
            User user = userService.findByEmail(email);
            String token = jwtTokenManager.generateToken(userService.loadUserByUsername(user.getUsername()));
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid OTP"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        User user = userService.validateUser(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        final String token = jwtTokenManager.generateToken(userService.loadUserByUsername(user.getUsername()));

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("userId", user.getId());

        return ResponseEntity.ok(response);
    }
}

