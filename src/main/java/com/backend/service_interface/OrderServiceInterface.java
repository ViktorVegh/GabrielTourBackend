package com.backend.service_interface;

import com.backend.dtos.OrderDTO;
import com.example.klientsoapclient.KlientObjednavkaListResult;
import com.example.objednavkasoapclient.ObjednavkaDetailResult;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;

public interface OrderServiceInterface {

    String createOrderList(KlientObjednavkaListResult result, Long id);

    int createOrderDetail(ObjednavkaDetailResult result);

    OrderDTO getOrderDetail(int id);
}
