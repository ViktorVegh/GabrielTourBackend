package com.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
public class Transportation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fields as per Profis definition
    private LocalDateTime pickupTime;           // CasNastupni
    private LocalDateTime dropoffTime;          // CasVystupni
    private LocalDateTime startDate;            // Datum
    private String transportReservationId;      // id_Doprava
    private String transportCapacityId;         // id_DopravaKapacita
    private String transportRouteId;            // id_DopravaTrasa
    private String seatingPlanId;               // id_ZasedaciPlan
    private String departureAirportCode;        // LetisteNastupni
    private String arrivalAirportCode;          // LetisteVystupni
    private String routeName;                   // Nazev
    private int reservedSeats;                  // Pocet
    private String notes;                       // Poznamka
    private String direction;                   // Smer
    private String pickupLocation;              // SvozMisto
    private String transportType;               // TypDoprava

    @ElementCollection
    private Map<String, String> externalValues; // Externi

    @ManyToMany
    private List<User> passengers;              // RezervaceDopravaCestujici

    // Constructors, Getters, and Setters

    public Transportation(LocalDateTime pickupTime, LocalDateTime dropoffTime, LocalDateTime startDate,
                          String transportReservationId, String transportCapacityId, String transportRouteId,
                          String seatingPlanId, String departureAirportCode, String arrivalAirportCode,
                          String routeName, int reservedSeats, String notes, List<User> passengers,
                          String direction, String pickupLocation, String transportType,
                          Map<String, String> externalValues) {
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.startDate = startDate;
        this.transportReservationId = transportReservationId;
        this.transportCapacityId = transportCapacityId;
        this.transportRouteId = transportRouteId;
        this.seatingPlanId = seatingPlanId;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.routeName = routeName;
        this.reservedSeats = reservedSeats;
        this.notes = notes;
        this.passengers = passengers;
        this.direction = direction;
        this.pickupLocation = pickupLocation;
        this.transportType = transportType;
        this.externalValues = externalValues;
    }

    public Transportation() {}

    // Getters and Setters for each field...

}
