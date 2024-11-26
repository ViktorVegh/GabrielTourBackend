package com.backend.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
public class Hotel {

    @Id
    private int id; // Unique numeric identifier fetched from ProfiTour

    private String name; // Hotel name (from ObjednavkaHotel)
    private int stars; // Number of stars for the hotel
    private String region; // Region associated with the hotel
    private String country; // Country associated with the hotel
    private String area; // Specific area within the region

    @ElementCollection
    private Map<String, String> externalValues; // External values as key-value pairs (Externi)

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccommodationReservation> reservations; // Reservations associated with this hotel

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }



    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Map<String, String> getExternalValues() {
        return externalValues;
    }

    public void setExternalValues(Map<String, String> externalValues) {
        this.externalValues = externalValues;
    }

    public List<AccommodationReservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<AccommodationReservation> reservations) {
        this.reservations = reservations;
    }


}
