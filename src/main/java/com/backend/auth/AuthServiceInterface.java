package com.backend.auth;

public interface AuthServiceInterface {
    // Registration
    String registerEmployee(String email, String password, String name, String profilePicture, String role);

    void registerUser(String email, String password, Long clientId);

    // Login
    String login(String email, String password);

    void logout();
}
