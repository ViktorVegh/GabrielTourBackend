package com.backend.controller;

import com.backend.auth.AuthService;
import com.backend.dtos.LoginRequest;
import com.backend.dtos.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getName(),
                registerRequest.getProfilePicture(),
                registerRequest.getRole()
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }


    @PostMapping("/logout")
    public String logout() {
        authService.logout();
        return "Logged out successfully";
    }
}
