package com.backend.service;

import com.backend.entity.Drive;
import com.backend.entity.TeeTime;
import com.backend.entity.TransportationReservation;
import com.backend.entity.User;
import com.backend.repository.HotelRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TeeTimeDriveFactory {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Drive> createDrivesForTeeTime(TeeTime teeTime) {
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
        toTeeTime.setUserIds(teeTime.getUsers().stream()
                .map(User::getProfisId) // Extract Profis IDs
                .collect(Collectors.toList()));
        drives.add(toTeeTime);

        // Golf Course to Hotel
        Drive fromTeeTime = new Drive();
        fromTeeTime.setTeeTime(teeTime);
        fromTeeTime.setDate(teeTime.getTeeTime().toLocalDate());
        fromTeeTime.setCustomReason("Golf Course to Hotel");
        fromTeeTime.setDeparturePlace(golfCourseName);
        fromTeeTime.setArrivalPlace(hotelName);
        fromTeeTime.setUserIds(teeTime.getUsers().stream()
                .map(User::getProfisId) // Extract Profis IDs
                .collect(Collectors.toList()));
        drives.add(fromTeeTime);

        return drives;
    }

    private String extractHotelName(TeeTime teeTime) {
        // Step 1: Get the first user's profisId
        Integer firstProfisId = teeTime.getUsers().stream()
                .map(User::getProfisId) // Extract Profis ID
                .map(Long::intValue)    // Convert Long to Integer if necessary
                .findFirst()            // Get the first Profis ID
                .orElseThrow(() -> new RuntimeException("No users associated with this TeeTime"));

        // Step 2: Find the user by Profis ID
        User user = userRepository.findByProfisId(firstProfisId)
                .orElseThrow(() -> new RuntimeException("No user found with Profis ID: " + firstProfisId));

        // Step 3: Query the hotel name directly
        return hotelRepository.findFirstHotelNameByUserId(user.getId().intValue())
                .orElse("Unknown Hotel");
    }





    private List<Long> extractUserIdsFromOrderDetail(TeeTime teeTime) {
        // Navigate TeeTime → Users → OrderUsers → OrderDetails → Travelers
        return teeTime.getUsers().stream()
                .flatMap(user -> user.getOrderUsers().stream())
                .flatMap(orderUser -> orderUser.getOrderDetail().getTransportationReservations().stream())
                .flatMap(reservation -> reservation.getOrderDetail().getTravelers().stream())
                .map(Long::valueOf)
                .distinct()
                .collect(Collectors.toList());
    }
}
