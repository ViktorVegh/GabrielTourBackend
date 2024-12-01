package com.backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

@Entity
public class Prices {
    @Id
    private int id;
    private String name;
    private BigDecimal price;

    private String currency; // Mena

    public Prices() {
    }

    public Prices(int id, String name, BigDecimal price, OrderDetail orderDetail) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.orderDetail = orderDetail;
    }


    public void setId(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetail orderDetail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
