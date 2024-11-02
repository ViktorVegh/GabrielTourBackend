package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Office extends Person {

    // Constructors
    public Office() {
        super();
    }

    public Office(String email, String password) {
        super(email, password);
    }
}