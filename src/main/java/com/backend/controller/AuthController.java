package com.backend.controller;

import com.backend.auth.AuthService;
import com.backend.dtos.LoginRequest;
import com.backend.dtos.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Register endpoint
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest registerRequest) {
        String token = authService.register(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getName(),
                registerRequest.getProfilePicture(),
                registerRequest.getRole()
        );


        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    // Login endpoint
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());


        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    // Logout endpoint
    @PostMapping("/logout")
    public String logout() {
        authService.logout();
        return "Logged out successfully";
    }
}
