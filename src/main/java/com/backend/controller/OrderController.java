package com.backend.controller;

import com.backend.auth.JwtHelper;
import com.backend.dtos.OrderDTO;
import com.backend.dtos.TeeTimeRequest;
import com.backend.profis_service.ProfisOrderService;
import com.backend.profis_service_interface.ProfisOrderServiceInterface;
import com.backend.service.OrderService;
import com.backend.service_interface.OrderServiceInterface;
import com.example.klientsoapclient.*;
import com.example.klientsoapclient.KlientKontakt;
import com.example.klientsoapclient.ObjednavkaKlient;
import com.example.objednavkasoapclient.*;
import com.example.objednavkasoapclient.IntegerNazev;
import jakarta.xml.bind.JAXBElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private OrderServiceInterface orderService;
    @Autowired
    private ProfisOrderServiceInterface profisOrderService;

    @PostMapping("/get-order")
    public ResponseEntity<String> CreateOrderList(@RequestBody Map<String, Object> body) {
        try {
            long id = Integer.parseInt(body.get("id").toString()); // Extract ID from the Map

            // Fetch order data using the service
            String result = profisOrderService.CreateOrderListRequest(id);



            // Return the XML response
            return ResponseEntity.ok()
                    .header("Content-Type", "application/xml")
                    .body(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                            "<s:Body>" +
                            "<Error>Error processing request.</Error>" +
                            "</s:Body>" +
                            "</s:Envelope>");
        }
    }
    @PostMapping("/create-orderDetails")
    public ResponseEntity<Integer> CreateOrderDetail(@RequestBody Map<String, Object> body) {
        try {
            int id = Integer.parseInt(body.get("id").toString()); // Extract ID from the Map
            // Fetch order data using the service
            int result = profisOrderService.CreateOrderDetailRequest(id);

            // Return the XML response
            return ResponseEntity.ok()
                    .header("Content-Type", "application/xml")
                    .body(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(0);
        }
    }

    @PreAuthorize("hasAnyAuthority('office','user')")
    @GetMapping("/get-orderDetails")
    public OrderDTO getOrderDetail(@RequestParam int id) {
        return orderService.getOrderDetail(id);
    }

}
