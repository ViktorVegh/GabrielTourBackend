package com.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_user")
public class OrderUser {

    @EmbeddedId
    private OrderUserId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private TourOrder tourOrder;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    private String klic; // Additional metadata

    // Constructors, Getters, and Setters

    public OrderUser() {}

    public OrderUser(TourOrder tourOrder, User user, String klic) {
        this.tourOrder = tourOrder;
        this.user = user;
        this.klic = klic;
        this.id = new OrderUserId(tourOrder.getId(), user.getId());
    }

    public OrderUserId getId() {
        return id;
    }

    public void setId(OrderUserId id) {
        this.id = id;
    }

    public TourOrder getTourOrder() {
        return tourOrder;
    }

    public void setTourOrder(TourOrder tourOrder) {
        this.tourOrder = tourOrder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getKlic() {
        return klic;
    }

    public void setKlic(String klic) {
        this.klic = klic;
    }
}

