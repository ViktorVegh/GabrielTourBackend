package com.backend.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DriveDTO {

    private Long id;
    private LocalDate date;
    private LocalDateTime pickupTime;
    private LocalDateTime dropoffTime;
    private String customReason;
    private Long driverId;
    private String departurePlace;
    private String arrivalPlace;
    private List<Long> userIds;
    private List<Integer> priceIds;

    // Constructor, Getters, and Setters
    public DriveDTO(Long id, LocalDate date, LocalDateTime pickupTime, LocalDateTime dropoffTime, String customReason, Long driverId, String departurePlace, String arrivalPlace, List<Long> userIds, List<Integer> priceIds) {
        this.id = id;
        this.date = date;
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.customReason = customReason;
        this.driverId = driverId;
        this.departurePlace = departurePlace;
        this.arrivalPlace = arrivalPlace;
        this.userIds = userIds;
        this.priceIds = priceIds;
    }

    public DriveDTO(){}

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

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getPriceIds(){return priceIds;}

    public void setPriceIds(List<Integer> priceIds){this.priceIds = priceIds;}
}
