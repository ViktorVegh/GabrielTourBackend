package com.backend.entity;


import jakarta.persistence.Entity;

@Entity
public class TourGuide extends Person {

    // Constructors
    public TourGuide() {
        super();
    }

    public TourGuide(String email, String password, String name, String profilePicture) {
        super(email, password, name, profilePicture);
    }
}
