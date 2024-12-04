package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrderDetail {

    @Id
    private int id; // Globální identifikátor aktuálního záznamu (ID from ProfiTour)

    private double totalPrice; // CenaCelkem
    private String pricing; // Ceny (Aktuální cenová kalkulace objednávky)
    private LocalDateTime startDate; // DatumOd
    private LocalDateTime endDate; // DatumDo
    private int numberOfDays; // Dni
    private int numberOfNights; // Noci
    private int adults; // Dospelych
    private int children; // Deti
    private int infants; // Infantu
    private String currency; // Mena
    private String paymentStatus; // StavPlatba
    private String name; // Nazev (Název objednávky)

    @ElementCollection
    private Map<String, String> externalValues; // Externi (Key-value external attributes)

    // Relationships
    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AccommodationReservation> accommodationReservations; // RezervaceUbytovani

    @OneToOne(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private OrderUser orderUser;


    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TransportationReservation> transportationReservations; // RezervaceDopravy

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<Prices> prices;

    @ElementCollection
    private List<String> travelers;

    private int termId; // id_Termin (ID of the booked term)
    private String stateOfOrder; // StavObjednavka (State of the order)

    public OrderDetail() {
    }

    public OrderDetail(int id, double totalPrice, String pricing, LocalDateTime startDate, LocalDateTime endDate, int numberOfDays, int numberOfNights, int adults, int children, int infants, String currency, String paymentStatus, String name, Map<String, String> externalValues, List<AccommodationReservation> accommodationReservations, List<TransportationReservation> transportationReservations, List<Prices> prices, List<String> travelers, int termId, String stateOfOrder) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.pricing = pricing;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.numberOfNights = numberOfNights;
        this.adults = adults;
        this.children = children;
        this.infants = infants;
        this.currency = currency;
        this.paymentStatus = paymentStatus;
        this.name = name;
        this.externalValues = externalValues;
        this.accommodationReservations = accommodationReservations;
        this.transportationReservations = transportationReservations;
        this.prices = prices;
        this.travelers = travelers;
        this.termId = termId;
        this.stateOfOrder = stateOfOrder;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPricing() {
        return pricing;
    }
    public void setOrderUsers(OrderUser orderUsers){
        this.orderUser=orderUsers;
    }
    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getInfants() {
        return infants;
    }

    public void setInfants(int infants) {
        this.infants = infants;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Map<String, String> getExternalValues() {
        return externalValues;
    }

    public void setExternalValues(Map<String, String> externalValues) {
        this.externalValues = externalValues;
    }

    public List<AccommodationReservation> getAccommodationReservations() {
        return accommodationReservations;
    }

    public void setAccommodationReservations(List<AccommodationReservation> accommodationReservations) {
        this.accommodationReservations = accommodationReservations;
    }

    public List<TransportationReservation> getTransportationReservations() {
        return transportationReservations;
    }

    public void setTransportationReservations(List<TransportationReservation> transportationReservations) {
        this.transportationReservations = transportationReservations;
    }

    public List<Prices> getPrices() {
        return prices;
    }

    public void setPrices(List<Prices> prices) {
        this.prices = prices;
    }

    public OrderUser getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(OrderUser orderUser) {
        this.orderUser = orderUser;
    }

    public List<String> getTravelers() {
        return travelers;
    }

    public void setTravelers(List<String> travelers) {
        this.travelers = travelers;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getStateOfOrder() {
        return stateOfOrder;
    }

    public void setStateOfOrder(String stateOfOrder) {
        this.stateOfOrder = stateOfOrder;
    }


}
