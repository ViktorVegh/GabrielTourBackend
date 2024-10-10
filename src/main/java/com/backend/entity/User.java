package com.backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class User extends Person {

    // Constructors
    public User() {
        super();
    }

    public User(String email, String password) {
        super(email, password);
    }

    public User(String email, String password, String name, String profilePicture) {
        super(email, password, name, profilePicture);
    }

}
