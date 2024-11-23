package com.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
public class Transportation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime pickupTime;
    private LocalDateTime dropoffTime;
    private LocalDateTime startDate;
    private String transportReservationId;
    private String transportCapacityId;
    private String transportRouteId;
    private String seatingPlanId;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String routeName;
    private int reservedSeats;
    private String notes;
    private String direction;
    private String pickupLocation;
    private String transportType;

    @ElementCollection
    private Map<String, String> externalValues;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private TourOrder tourOrder;

    @ManyToMany
    private List<User> passengers;

    // Constructors, Getters, and Setters

    public Transportation() {}

    public Transportation(LocalDateTime pickupTime, LocalDateTime dropoffTime, LocalDateTime startDate,
                          String transportReservationId, String transportCapacityId, String transportRouteId,
                          String seatingPlanId, String departureAirportCode, String arrivalAirportCode,
                          String routeName, int reservedSeats, String notes, List<User> passengers,
                          String direction, String pickupLocation, String transportType,
                          Map<String, String> externalValues, TourOrder tourOrder) {
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.startDate = startDate;
        this.transportReservationId = transportReservationId;
        this.transportCapacityId = transportCapacityId;
        this.transportRouteId = transportRouteId;
        this.seatingPlanId = seatingPlanId;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.routeName = routeName;
        this.reservedSeats = reservedSeats;
        this.notes = notes;
        this.direction = direction;
        this.pickupLocation = pickupLocation;
        this.transportType = transportType;
        this.externalValues = externalValues;
        this.passengers = passengers;
        this.tourOrder = tourOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(LocalDateTime dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getTransportReservationId() {
        return transportReservationId;
    }

    public void setTransportReservationId(String transportReservationId) {
        this.transportReservationId = transportReservationId;
    }

    public TourOrder getTourOrder() {
        return tourOrder;
    }

    public void setTourOrder(TourOrder tourOrder) {
        this.tourOrder = tourOrder;
    }

    // Add remaining getters and setters...
}

