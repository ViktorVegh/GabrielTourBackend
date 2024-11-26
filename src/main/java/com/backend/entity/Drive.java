package com.backend.entity;

import jakarta.persistence.*;

@Entity
public class Drive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Enum for predefined drive reasons
    public enum Reason {
        TEE_TIME,
        AIRPORT,
        OTHER
    }

    // Drive reason with the option for custom reasons
    @Enumerated(EnumType.STRING)
    private Reason reason;

    private String customReason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tee_time_id")
    private TeeTime teeTime;


    // Constructors

    // Primary constructor with basic fields
    public Drive(Reason reason, String customReason, TeeTime teeTime) {
        this.reason = reason;
        this.customReason = reason == Reason.OTHER ? customReason : null;
        this.teeTime = teeTime;
    }

    // Constructor with driver field
    public Drive(Reason reason, String customReason, Driver driver, TeeTime teeTime) {
        this.reason = reason;
        this.customReason = reason == Reason.OTHER ? customReason : null;
        this.driver = driver;
        this.teeTime = teeTime;
    }

    // Default constructor
    public Drive() {}

    // Getters and Setters for all fields

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
        if (reason != Reason.OTHER) {
            this.customReason = null; // Clear custom reason if a standard reason is selected
        }
    }

    public String getCustomReason() {
        return customReason;
    }

    public void setCustomReason(String customReason) {
        if (this.reason == Reason.OTHER) {
            this.customReason = customReason; // Only set if reason is OTHER
        } else {
            throw new UnsupportedOperationException("Custom reason is only applicable if reason is OTHER.");
        }
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


}
