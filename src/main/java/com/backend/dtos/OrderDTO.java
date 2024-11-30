package com.backend.dtos;

import com.backend.entity.*;
import java.util.List;

public class OrderDTO {
    private OrderDetail orderDetail;
    private List<Prices> pricesList;
    private List<AccommodationReservation> accommodationReservation;
    private List<TransportationReservation> transportationReservation;

    public OrderDTO(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public List<Prices> getPricesList() {
        return pricesList;
    }

    public void setPricesList(List<Prices> pricesList) {
        this.pricesList = pricesList;
    }

    public List<AccommodationReservation> getAccommodationReservation() {
        return accommodationReservation;
    }

    public void setAccommodationReservation(List<AccommodationReservation> accommodationReservation) {
        this.accommodationReservation = accommodationReservation;
    }

    public List<TransportationReservation> getTransportationReservation() {
        return transportationReservation;
    }

    public void setTransportationReservation(List<TransportationReservation> transportationReservation) {
        this.transportationReservation = transportationReservation;
    }
}
