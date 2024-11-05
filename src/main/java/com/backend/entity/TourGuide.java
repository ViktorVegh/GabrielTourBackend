package com.backend.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
public class TourGuide extends Person {

    // Constructors
    public TourGuide() {
        super();
    }

    public TourGuide(String email, String password, String name, String profilePicture,String role) {
        super(email, password, name, profilePicture,role);
    }
}
