package com.backend.entity;

import jakarta.persistence.*;

import java.util.Optional;

@MappedSuperclass
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String name;

    @Column
    private String profilePicture;

    // Default Constructor
    public Person() {}

    // Constructor with name and profile picture (for Driver and TourGuide)
    public Person(String email, String password, String name, String profilePicture) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.profilePicture = profilePicture;
    }

    // Constructor without name and profile picture (for User)
    public Person(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getProfilePicture() {
        return Optional.ofNullable(profilePicture);
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
