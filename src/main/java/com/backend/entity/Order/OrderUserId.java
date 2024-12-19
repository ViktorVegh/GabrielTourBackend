package com.backend.entity.Order;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderUserId implements Serializable {

    private int orderId;
    private Long userId;

    // Constructors, Getters, Setters, Equals, and HashCode

    public OrderUserId() {}

    public OrderUserId(int orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderUserId that = (OrderUserId) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId);
    }
}
