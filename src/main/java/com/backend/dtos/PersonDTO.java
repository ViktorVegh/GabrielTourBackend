package com.backend.dtos;

public class PersonDTO {
    private Long id;
    private String email;
    private String name;
    private String role;
    private String profilePicture;

    // Constructors
    public PersonDTO() {}

    public PersonDTO(Long id, String email, String name, String role, String profilePicture) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    public PersonDTO(Long id, String role) {
        this.id = id;
        this.role = role;
    }


    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
