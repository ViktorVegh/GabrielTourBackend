package com.backend.entity.Person;

import jakarta.persistence.Entity;

@Entity
public class Office extends Person {

    // Constructors
    public Office() {
        super();
    }

    public Office(String email, String password, String role) {
        super(email, password, role);
    }
}