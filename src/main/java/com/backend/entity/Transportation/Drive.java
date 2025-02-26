package com.backend.entity.Transportation;

import com.backend.entity.Person.Driver;
import com.backend.entity.TeeTime.TeeTime;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Drive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalDateTime pickupTime;
    private LocalDateTime dropoffTime;

    private String customReason;

    private String departurePlace;
    private String arrivalPlace;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ElementCollection
    @CollectionTable(name = "drive_prices", joinColumns = @JoinColumn(name = "drive_id"))
    @Column(name = "price_id")
    private List<Integer> priceIds;


    @OneToOne(fetch = FetchType.LAZY, cascade = {}, orphanRemoval = false)
    @JoinColumn(name = "tee_time_id", nullable = true) // Allow null here
    private TeeTime teeTime;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "transportation_reservation_id")
    private TransportationReservation transportationReservation;

    @ElementCollection
    @CollectionTable(name = "drive_user_ids", joinColumns = @JoinColumn(name = "drive_id"))
    @Column(name = "user_id")
    private List<Long> userIds;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getCustomReason() {
        return customReason;
    }

    public void setCustomReason(String customReason) {
        this.customReason = customReason;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getArrivalPlace() {
        return arrivalPlace;
    }

    public void setArrivalPlace(String arrivalPlace) {
        this.arrivalPlace = arrivalPlace;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public TeeTime getTeeTime() {
        return teeTime;
    }

    public void setTeeTime(TeeTime teeTime) {
        this.teeTime = teeTime;
    }

    public TransportationReservation getTransportationReservation() {
        return transportationReservation;
    }

    public void setTransportationReservation(TransportationReservation transportationReservation) {
        this.transportationReservation = transportationReservation;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getPriceIds() {
        return priceIds;
    }

    public void setPriceIds(List<Integer> priceIds) {
        this.priceIds = priceIds;
    }
}
