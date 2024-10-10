package com.backend.entity;


import jakarta.persistence.Entity;

@Entity
public class Driver extends Person {

    // Constructors
    public Driver() {
        super();
    }

    public Driver(String email, String password, String name, String profilePicture) {
        super(email, password, name, profilePicture);
    }
}
