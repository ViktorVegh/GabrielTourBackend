package com.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
public class AccommodationReservation {

    @Id
    private int id; // Globální identifikátor aktuálního záznamu from ProfiTour

    private LocalDateTime startDate; // DatumOd
    private int beds; // Luzek
    private int extraBeds; // Pristylek
    private int numberOfNights; // Noci
    private String accommodationName; // NazevUbytovani
    private String note; // Poznamka
    private String mealType; // TypStrava

    @ElementCollection
    private Map<String, String> externalValues; // Externi

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel; // Associated Hotel details (ObjednavkaHotel)

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetail orderDetail; // Link to OrderDetail (RezervaceUbytovani belongs to an Order)


    @ManyToMany
    @JoinTable(
            name = "accommodation_reservation_travelers",
            joinColumns = @JoinColumn(name = "accommodation_reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "profis_id", referencedColumnName = "profis_id")
    )
    private List<User> travelers;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getExtraBeds() {
        return extraBeds;
    }

    public void setExtraBeds(int extraBeds) {
        this.extraBeds = extraBeds;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Map<String, String> getExternalValues() {
        return externalValues;
    }

    public void setExternalValues(Map<String, String> externalValues) {
        this.externalValues = externalValues;
    }

    public Hotel getObjednavkaHotel() {
        return hotel;
    }

    public void setObjednavkaHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<User> getTravelers() {
        return travelers;
    }

    public void setTravelers(List<User> travelers) {
        this.travelers = travelers;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
