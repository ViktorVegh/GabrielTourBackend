package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TourOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int orderNumber;
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "tourOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transportation> transportations; // Corrected mappedBy

    @OneToMany(mappedBy = "tourOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderUser> orderUsers; // Corrected mappedBy

    // Constructors, Getters, and Setters

    public TourOrder() {}

    public TourOrder(int orderNumber, LocalDateTime orderDate, List<Transportation> transportations) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.transportations = transportations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<Transportation> getTransportations() {
        return transportations;
    }

    public void setTransportations(List<Transportation> transportations) {
        this.transportations = transportations;
    }

    public List<OrderUser> getOrderUsers() {
        return orderUsers;
    }

    public void setOrderUsers(List<OrderUser> orderUsers) {
        this.orderUsers = orderUsers;
    }
}

