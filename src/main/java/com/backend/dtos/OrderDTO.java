package com.backend.dtos;

import com.backend.entity.*;
import org.hibernate.mapping.List;

public class OrderDTO {
    private OrderDetail orderDetail;
    private Prices pricesList;
    private AccommodationReservation accommodationReservation;
    private TransportationReservation transportationReservation;

    public OrderDTO(OrderDetail orderDetail, Prices prices, AccommodationReservation accommodationReservation, TransportationReservation transportationReservation) {
        this.orderDetail = orderDetail;
        this.pricesList = prices;
        this.accommodationReservation = accommodationReservation;
        this.transportationReservation = transportationReservation;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public AccommodationReservation getAccommodationReservation() {
        return accommodationReservation;
    }

    public void setAccommodationReservation(AccommodationReservation accommodationReservation) {
        this.accommodationReservation = accommodationReservation;
    }

    public TransportationReservation getTransportationReservation() {
        return transportationReservation;
    }

    public void setTransportationReservation(TransportationReservation transportationReservation) {
        this.transportationReservation = transportationReservation;
    }
}
