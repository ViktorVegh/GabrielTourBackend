package com.backend.dtos.Drive;

import com.backend.entity.Person.Driver;

import java.time.LocalDateTime;

public class EditDriveRequest {

    private LocalDateTime pickupTime;
    private LocalDateTime dropoffTime;
    private Driver driver;

    // Getters and Setters
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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
