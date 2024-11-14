package com.backend.entity;

import java.time.LocalDate;
import java.util.List;

public class Accommodation {

    // Start date of the stay
    private LocalDate startDate;

    // Unique identifier for the accommodation reservation
    private String reservationId;

    // Number of beds in the accommodation
    private int beds;

    // Name of the reserved accommodation (hotel or resort)
    private String accommodationName;

    // Number of nights for the stay
    private int nights;

    // Notes regarding the accommodation reservation
    private String notes;

    // Number of extra beds in the accommodation
    private int extraBeds;

    // Type of meal plan (e.g., breakfast, half-board)
    private String mealPlan;

    // Additional hotel information if available
    private Hotel hotelDetails;

    // List of travelers in this accommodation reservation
    private List<User> accommodationTravelers;

    // Getters and Setters

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getExtraBeds() {
        return extraBeds;
    }

    public void setExtraBeds(int extraBeds) {
        this.extraBeds = extraBeds;
    }

    public String getMealPlan() {
        return mealPlan;
    }

    public void setMealPlan(String mealPlan) {
        this.mealPlan = mealPlan;
    }

    public Hotel getHotelDetails() {
        return hotelDetails;
    }

    public void setHotelDetails(Hotel hotelDetails) {
        this.hotelDetails = hotelDetails;
    }

    public List<User> getAccommodationTravelers() {
        return accommodationTravelers;
    }

    public void setAccommodationTravelers(List<User> accommodationTravelers) {
        this.accommodationTravelers = accommodationTravelers;
    }
}
