package com.backend.service;

import com.backend.entity.Transportation.Drive;
import com.backend.entity.Transportation.TransportationReservation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransportationDriveFactory {

    public static List<Drive> createDrivesForTransportation(TransportationReservation reservation) {
        List<Drive> drives = new ArrayList<>();
        String departure = reservation.getDepartureAirportName() != null ? reservation.getDepartureAirportName() : "Unknown Departure";
        String arrival = reservation.getArrivalAirportName() != null ? reservation.getArrivalAirportName() : "Unknown Arrival";

        // Create drive for "tam" route
        if ("tam".equalsIgnoreCase(reservation.getRouteName())) {
            Drive toDestination = new Drive();
            toDestination.setTransportationReservation(reservation);
            toDestination.setDate(reservation.getDropoffTime().toLocalDate());
            toDestination.setCustomReason("Airport to Hotel");
            toDestination.setDeparturePlace(departure);
            toDestination.setArrivalPlace(arrival);
            toDestination.setUserIds(extractUserIdsFromOrderDetail(reservation));
            drives.add(toDestination);
        }

        // Create drive for "zpet" route
        if ("zpet".equalsIgnoreCase(reservation.getRouteName())) {
            Drive fromDestination = new Drive();
            fromDestination.setTransportationReservation(reservation);
            fromDestination.setDate(reservation.getPickupTime().toLocalDate());
            fromDestination.setCustomReason("Hotel to Airport");
            fromDestination.setDeparturePlace(arrival);
            fromDestination.setArrivalPlace(departure);
            fromDestination.setUserIds(extractUserIdsFromOrderDetail(reservation));
            drives.add(fromDestination);
        }

        return drives;
    }

    private static List<Long> extractUserIdsFromOrderDetail(TransportationReservation reservation) {
        if (reservation.getOrderDetail() == null || reservation.getOrderDetail().getTravelers() == null) {
            return new ArrayList<>(); // Return empty list if no travelers are found
        }

        // Extract traveler IDs from the OrderDetail
        return reservation.getOrderDetail().getTravelers().stream()
                .map(Long::valueOf) // Convert String to Long
                .collect(Collectors.toList());
    }
}
