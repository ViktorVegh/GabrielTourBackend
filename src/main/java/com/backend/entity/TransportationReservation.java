package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
public class TransportationReservation {

    @Id
    private int id; // Globální identifikátor aktuálního záznamu (ID from ProfiTour)

    private LocalDateTime pickupTime; // CasNastupni (Nástupní datum a čas)
    private LocalDateTime dropoffTime; // CasVystupni (Výstupní datum a čas)
    private LocalDateTime startDate; // Datum (Datum začátku dopravy)
    private int transportId; // id_Doprava (ID rezervované dopravy)
    private int transportCapacityId; // id_DopravaKapacita (ID rezervované kapacity dopravy)
    private int transportRouteId; // id_DopravaTrasa (ID rezervované trasy dopravy)
    private int seatingPlanId; // id_ZasedaciPlan (ID zasedacího plánu)
    private String departureAirportCode; // LetisteNastupni (Nástupní letiště)
    private String arrivalAirportCode; // LetisteVystupni (Výstupní letiště)
    private String departureAirportName; // LetisteNastupni (Nástupní letiště)
    private String arrivalAirportName; // LetisteVystupni (Výstupní letiště)
    private String routeName; // Nazev (Název trasy dopravy)
    private int reservedSeats; // Pocet (Počet rezervovaných míst)
    private String notes; // Poznamka (Poznámka k rezervaci dopravy)
    private String direction; // Smer (Směr rezervace dopravy tam nebo zpět)
    private String pickupLocation; // SvozMisto (Svozové místo)
    private int transportType; // TypDoprava (20 for bus, 40 for plane)

    @ElementCollection
    private Map<String, String> externalValues; // Externi (Key-value pairs for additional attributes)

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private OrderDetail orderDetail; // Link to OrderDetail (RezervaceDoprava belongs to an Order)

    @ManyToMany
    @JoinTable(
            name = "transportation_reservation_passengers",
            joinColumns = @JoinColumn(name = "transportation_reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "profis_id", referencedColumnName = "profis_id")
    )
    private List<User> passengers; // RezervaceDopravaCestujici (List of assigned passengers)

    // Constructors, Getters, and Setters

    public TransportationReservation() {}

    public TransportationReservation(LocalDateTime pickupTime, LocalDateTime dropoffTime, LocalDateTime startDate,
                                     int transportId, int transportCapacityId, int transportRouteId,
                                     int seatingPlanId, String departureAirportCode, String arrivalAirportCode,
                                     String routeName, int reservedSeats, String notes, String direction,
                                     String pickupLocation, int transportType, Map<String, String> externalValues,
                                     List<User> passengers, OrderDetail orderDetail) {
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.startDate = startDate;
        this.transportId = transportId;
        this.transportCapacityId = transportCapacityId;
        this.transportRouteId = transportRouteId;
        this.seatingPlanId = seatingPlanId;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.routeName = routeName;
        this.reservedSeats = reservedSeats;
        this.notes = notes;
        this.direction = direction;
        this.pickupLocation = pickupLocation;
        this.transportType = transportType;
        this.externalValues = externalValues;
        this.passengers = passengers;
        this.orderDetail = orderDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getTransportId() {
        return transportId;
    }

    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    public int getTransportCapacityId() {
        return transportCapacityId;
    }

    public void setTransportCapacityId(int transportCapacityId) {
        this.transportCapacityId = transportCapacityId;
    }

    public int getTransportRouteId() {
        return transportRouteId;
    }

    public void setTransportRouteId(int transportRouteId) {
        this.transportRouteId = transportRouteId;
    }

    public int getSeatingPlanId() {
        return seatingPlanId;
    }

    public void setSeatingPlanId(int seatingPlanId) {
        this.seatingPlanId = seatingPlanId;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(int reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public int getTransportType() {
        return transportType;
    }

    public void setTransportType(int transportType) {
        this.transportType = transportType;
    }

    public Map<String, String> getExternalValues() {
        return externalValues;
    }

    public void setExternalValues(Map<String, String> externalValues) {
        this.externalValues = externalValues;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<User> passengers) {
        this.passengers = passengers;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }


}
