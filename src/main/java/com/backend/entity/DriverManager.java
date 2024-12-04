package com.backend.entity;

import jakarta.persistence.Entity;

@Entity
public class DriverManager extends Person {

    // Constructors
    public DriverManager() {
        super();
    }

    public DriverManager(String email, String password, String name, String profilePicture, String role) {
        super(email, password, name, profilePicture,role);
    }
}
