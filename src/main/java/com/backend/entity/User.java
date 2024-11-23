package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "app_user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends Person {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderUser> orderUsers; // Relationship to orders via OrderUser

    @ManyToMany(mappedBy = "users")
    private List<TeeTime> teeTimes; // Independent relationship

    private Long profis_id;

    // Constructors, Getters, and Setters

    public User() {
        super();
    }

    public User(String email, String password, String role) {
        super(email, password, role);
    }

    public User(String email, String password, String name, String profilePicture, String role) {
        super(email, password, name, profilePicture, role);
    }

    public User(String email, String encryptedPassword, Long profis_id, String role) {
        super(email, encryptedPassword, role);
        this.profis_id = profis_id;
    }

    public Long getProfisId() {
        return profis_id;
    }

    public void setProfisId(Long profis_id) {
        this.profis_id = profis_id;
    }

    public List<OrderUser> getOrderUsers() {
        return orderUsers;
    }

    public void setOrderUsers(List<OrderUser> orderUsers) {
        this.orderUsers = orderUsers;
    }

    public List<TeeTime> getTeeTimes() {
        return teeTimes;
    }

    public void setTeeTimes(List<TeeTime> teeTimes) {
        this.teeTimes = teeTimes;
    }
}
