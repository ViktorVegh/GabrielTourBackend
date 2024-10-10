package com.backend.dtos;

public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String profilePicture;
    private String role;

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name == null ? "" : name;  // Default to empty string if null
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture == null ? "" : profilePicture;  // Default to empty string if null
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getRole() {
        return role == null ? "user" : role;  // Default to "user" if role is null
    }

    public void setRole(String role) {
        this.role = role;
    }
}
