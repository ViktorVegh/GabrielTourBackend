package com.backend.dtos.Acommodation;

import java.time.LocalDate;
import java.util.List;

public class AccommodationDTO {

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
    private HotelDTO hotelDetails;

    // List of traveler IDs in this accommodation reservation
    private List<Long> accommodationTravelerIds;

    // Constructors
    public AccommodationDTO() {
    }

    public AccommodationDTO(LocalDate startDate, String reservationId, int beds, String accommodationName,
                            int nights, String notes, int extraBeds, String mealPlan, HotelDTO hotelDetails,
                            List<Long> accommodationTravelerIds) {
        this.startDate = startDate;
        this.reservationId = reservationId;
        this.beds = beds;
        this.accommodationName = accommodationName;
        this.nights = nights;
        this.notes = notes;
        this.extraBeds = extraBeds;
        this.mealPlan = mealPlan;
        this.hotelDetails = hotelDetails;
        this.accommodationTravelerIds = accommodationTravelerIds;
    }

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

    public HotelDTO getHotelDetails() {
        return hotelDetails;
    }

    public void setHotelDetails(HotelDTO hotelDetails) {
        this.hotelDetails = hotelDetails;
    }

    public List<Long> getAccommodationTravelerIds() {
        return accommodationTravelerIds;
    }

    public void setAccommodationTravelerIds(List<Long> accommodationTravelerIds) {
        this.accommodationTravelerIds = accommodationTravelerIds;
    }
}
