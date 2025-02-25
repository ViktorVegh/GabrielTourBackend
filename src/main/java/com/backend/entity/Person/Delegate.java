package com.backend.entity.Person;


import jakarta.persistence.Entity;

@Entity
public class Delegate extends Person {

    // Constructors
    public Delegate() {
        super();
    }

    public Delegate(String email, String password, String name, String profilePicture, String role) {
        super(email, password, name, profilePicture,role);
    }
}
