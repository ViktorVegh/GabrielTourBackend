package com.backend.entity.Person;

import jakarta.persistence.Entity;

@Entity
public class Driver extends Person {

    // Constructors
    public Driver() {
        super();
    }

    public Driver(String email, String password, String name, String profilePicture, String role) {
        super(email, password, name, profilePicture,role);
    }

}

