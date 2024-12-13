package com.backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

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