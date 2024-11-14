package com.backend.entity;

import java.util.Map;

public class Hotel {

    // External values as key-value pairs
    private Map<String, String> externalValues;

    // Number of stars for the hotel
    private int stars;

    // Unique identifier
    private String id;

    // Hotel name
    private String name;

    // Region associated with the hotel (from RegionList)
    private String region;

    // Indicator for fractional star rating (e.g., 3.5 stars)
    private boolean hasHalfStar;

    // Country associated with the hotel (from CountryList)
    private String country;

    // Specific area within the region (from AreaList)
    private String area;

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

    public boolean hasHalfStar() {
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
