package com.backend.dtos;

import java.util.Map;

public class HotelDTO {

    // External values as key-value pairs
    private Map<String, String> externalValues;

    // Number of stars for the hotel
    private int stars;

    // Unique identifier
    private String id;

    // Hotel name
    private String name;

    // Region associated with the hotel
    private String region;

    // Indicator for fractional star rating (e.g., 3.5 stars)
    private boolean hasHalfStar;

    // Country associated with the hotel
    private String country;

    // Specific area within the region
    private String area;

    // Constructors
    public HotelDTO() {
    }

    public HotelDTO(Map<String, String> externalValues, int stars, String id, String name, String region,
                    boolean hasHalfStar, String country, String area) {
        this.externalValues = externalValues;
        this.stars = stars;
        this.id = id;
        this.name = name;
        this.region = region;
        this.hasHalfStar = hasHalfStar;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public boolean isHasHalfStar() {
        return hasHalfStar;
    }

    public void setHasHalfStar(boolean hasHalfStar) {
        this.hasHalfStar = hasHalfStar;
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
