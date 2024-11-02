package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User extends Person {
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<TeeTime> teeTimes;

    // Constructors
    public User() {
        super();
    }

    public User(String email, String password,String role) {
        super(email, password, role);
    }

    public User(String email, String password, String name, String profilePicture,String role) {
        super(email, password, name, profilePicture,role);
    }

    // One-to-Many relationship with TeeTime
    @OneToMany(mappedBy = "user")
    public List<TeeTime> getTeeTimes() {
        return teeTimes;
    }

    public void setTeeTimes(List<TeeTime> teeTimes) {
        this.teeTimes = teeTimes;
    }
}
