package com.backend.service;

import com.backend.entity.Drive;
import com.backend.entity.TeeTime;
import com.backend.entity.TransportationReservation;
import com.backend.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeeTimeDriveFactory {

    public static List<Drive> createDrivesForTeeTime(TeeTime teeTime) {
        List<Drive> drives = new ArrayList<>();
        String hotelName = extractHotelName(teeTime);
        String golfCourseName = teeTime.getGolfCourse() != null ? teeTime.getGolfCourse().getName() : "Unknown Golf Course";

        // Tee Time to Golf Course
        Drive toTeeTime = new Drive();
        toTeeTime.setTeeTime(teeTime);
        toTeeTime.setDate(teeTime.getTeeTime().toLocalDate());
        toTeeTime.setCustomReason("Hotel to Golf Course");
        toTeeTime.setDeparturePlace(hotelName);
        toTeeTime.setArrivalPlace(golfCourseName);
        toTeeTime.setUserIds(extractUserIdsFromOrderDetail(teeTime)); // Updated method to retrieve user IDs
        drives.add(toTeeTime);

        // Golf Course to Hotel
        Drive fromTeeTime = new Drive();
        fromTeeTime.setTeeTime(teeTime);
        fromTeeTime.setDate(teeTime.getTeeTime().toLocalDate());
        fromTeeTime.setCustomReason("Golf Course to Hotel");
        fromTeeTime.setDeparturePlace(golfCourseName);
        fromTeeTime.setArrivalPlace(hotelName);
        fromTeeTime.setUserIds(extractUserIdsFromOrderDetail(teeTime)); // Updated method to retrieve user IDs
        drives.add(fromTeeTime);

        return drives;
    }

    private static String extractHotelName(TeeTime teeTime) {
        return teeTime.getUsers().stream()
                .flatMap(user -> user.getOrderUsers().stream())
                .flatMap(orderUser -> orderUser.getOrderDetail().getAccommodationReservations().stream())
                .map(reservation -> reservation.getObjednavkaHotel().getName())
                .findFirst()
                .orElse("Unknown Hotel");
    }

    private static List<Long> extractUserIdsFromOrderDetail(TeeTime teeTime) {
        return teeTime.getUsers().stream()
                .flatMap(user -> user.getOrderUsers().stream())
                .flatMap(orderUser -> orderUser.getOrderDetail().getTransportationReservations().stream())
                .flatMap(reservation -> reservation.getOrderDetail().getTravelers().stream()) // Retrieve travelers from OrderDetail
                .map(Long::valueOf)
                .distinct()
                .collect(Collectors.toList());
    }

}

