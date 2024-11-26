package com.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
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
    private String reservationStatus; // StavRezervace
    private String paymentStatus; // StavPlatba
    private String contractStatus; // StavSmlouva
    private String requestStatus; // StavVyzadani
    private String instructionsStatus; // StavPokyny
    private String mealType; // TypStrava
    private String transportationType; // TypDoprava
    private LocalDateTime createdAt; // Vytvoreno
    private String name; // Nazev (Název objednávky)

    @ElementCollection
    private Map<String, String> externalValues; // Externi (Key-value external attributes)

    // Relationships
    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccommodationReservation> accommodationReservations; // RezervaceUbytovani

    @OneToOne(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderUser orderUser;


    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransportationReservation> transportationReservations; // RezervaceDopravy


    @ElementCollection
    private List<String> travelers; // Cestujici (Active traveling individuals)

    // Other fields
    private String airport; // Letiste (Airport, optional)
    private LocalDateTime reservationValidity; // RezervaceDo (Validity of reservation if "Reserved" status)
    private int termId; // id_Termin (ID of the booked term)
    private String stateOfOrder; // StavObjednavka (State of the order)
    private String agreementDetails; // Souhlasy (Details of agreements)

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

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getInstructionsStatus() {
        return instructionsStatus;
    }

    public void setInstructionsStatus(String instructionsStatus) {
        this.instructionsStatus = instructionsStatus;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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


    public List<String> getTravelers() {
        return travelers;
    }

    public void setTravelers(List<String> travelers) {
        this.travelers = travelers;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public LocalDateTime getReservationValidity() {
        return reservationValidity;
    }

    public void setReservationValidity(LocalDateTime reservationValidity) {
        this.reservationValidity = reservationValidity;
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

    public String getAgreementDetails() {
        return agreementDetails;
    }

    public void setAgreementDetails(String agreementDetails) {
        this.agreementDetails = agreementDetails;
    }


}
