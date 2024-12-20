package com.backend.entity.Order;

import com.backend.entity.Person.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "order_user")
public class OrderUser {

    @EmbeddedId
    private OrderUserId id;

    @OneToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderDetail orderDetail;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    private String klic; // Additional metadata

    // Constructors, Getters, and Setters

    public OrderUser() {}

    public OrderUser(OrderDetail orderDetail, User user, String klic) {
        this.orderDetail = orderDetail;
        this.user = user;
        this.klic = klic;
        this.id = new OrderUserId(orderDetail.getId(), user.getId());
    }

    public OrderUserId getId() {
        return id;
    }

    public void setId(OrderUserId id) {
        this.id = id;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
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

