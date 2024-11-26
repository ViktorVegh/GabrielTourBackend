package com.backend.dtos;

import java.util.Map;

public class HotelDTO {

    // External values as key-value pairs
    private Map<String, String> externalValues;

    // Number of stars for the hotel
    private int stars;

    // Unique identifier
    private int id;

    // Hotel name
    private String name;

    // Region associated with the hotel
    private String region;

    // Country associated with the hotel
    private String country;

    // Specific area within the region
    private String area;

    // Constructors
    public HotelDTO() {
    }

    public HotelDTO(Map<String, String> externalValues, int stars, int id, String name, String region,
              String country, String area) {
        this.externalValues = externalValues;
        this.stars = stars;
        this.id = id;
        this.name = name;
        this.region = region;
        this.country = country;
        this.area = area;
    }

    // Getters and Setters
    public Map<String, String> getExternalValues() {
        return externalValues;
    }

    public void setExternalValues(Map<String, String> externalValues) {
        this.externalValues = externalValues;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

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
}
