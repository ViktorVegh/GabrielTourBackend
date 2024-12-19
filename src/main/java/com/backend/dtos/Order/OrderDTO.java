package com.backend.dtos.Order;

import com.backend.entity.Order.OrderDetail;

public class OrderDTO {
    private OrderDetail orderDetail;


    public OrderDTO(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }


    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

}
