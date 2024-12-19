package com.backend.service_interface;

import com.backend.dtos.Order.OrderDTO;
import com.example.klientsoapclient.KlientObjednavkaListResult;
import com.example.objednavkasoapclient.ObjednavkaDetailResult;

public interface OrderServiceInterface {

    String createOrderList(KlientObjednavkaListResult result, Long id);

    int createOrderDetail(ObjednavkaDetailResult result);

    OrderDTO getOrderDetail(int id);
}
