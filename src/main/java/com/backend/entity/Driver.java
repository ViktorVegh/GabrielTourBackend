package com.backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

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

