package com.backend.dtos;

import com.backend.entity.*;
import java.util.List;

public class OrderDTO {
    private OrderDetail orderDetail;


    public OrderDTO(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }


    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

}
