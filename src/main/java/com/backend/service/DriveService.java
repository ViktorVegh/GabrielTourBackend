package com.backend.service;
import com.backend.entity.Driver;

import com.backend.entity.Drive;
import com.backend.entity.TeeTime;
import com.backend.entity.TransportationReservation;
import com.backend.entity.User;
import com.backend.repository.*;
import com.backend.service_interface.DriveServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.backend.entity.Hotel;

@Service
public class DriveService implements DriveServiceInterface {

    @Autowired
    private TeeTimeRepository teeTimeRepository;

    @Autowired
    private TransportationReservationRepository transportationReservationRepository;

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TourGuideRepository tourGuideRepository;

    /**
     * Fetch drives for the given date range.
     */
    @Override
    public List<Drive> getDrivesForDateRange(LocalDate startDate, LocalDate endDate) {
        List<Drive> drives = new ArrayList<>();

        // Fetch existing drives
        List<Drive> existingDrives = driveRepository.findAllByDateBetween(startDate, endDate);
        drives.addAll(existingDrives);

        // Create drives for TeeTimes
        List<TeeTime> teeTimes = teeTimeRepository.findAllByTeeTimeBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );

        for (TeeTime teeTime : teeTimes) {
            if (existingDrives.stream().noneMatch(d -> d.getTeeTime() != null && d.getTeeTime().equals(teeTime))) {
                drives.addAll(createDrivesForTeeTime(teeTime));
            }
        }

        // Create drives for TransportationReservations
        List<TransportationReservation> reservations = transportationReservationRepository
                .findAllByPickupTimeBetweenOrDropoffTimeBetween(
                        startDate.atStartOfDay(),
                        endDate.atTime(23, 59, 59),
                        startDate.atStartOfDay(),
                        endDate.atTime(23, 59, 59)
                );

        for (TransportationReservation reservation : reservations) {
            if (existingDrives.stream().noneMatch(d -> d.getTransportationReservation() != null &&
                    d.getTransportationReservation().equals(reservation))) {
                drives.addAll(createDrivesForTransportation(reservation));
            }
        }

        driveRepository.saveAll(drives);
        return drives;
    }

    @Override
    public List<Drive> createDrivesForTeeTime(TeeTime teeTime) {
        List<Drive> drives = new ArrayList<>();

        // Fetch hotel name from accommodation reservation
        String hotelName = teeTime.getUsers().stream()
                .flatMap(user -> user.getOrderUsers().stream())
                .map(orderUser -> orderUser.getOrderDetail())
                .filter(orderDetail -> orderDetail != null && orderDetail.getAccommodationReservations() != null)
                .flatMap(orderDetail -> orderDetail.getAccommodationReservations().stream())
                .map(reservation -> reservation.getObjednavkaHotel())
                .filter(hotel -> hotel != null)
                .map(Hotel::getName)
                .findFirst()
                .orElse("Unknown Hotel");

        // Fetch golf course name
        String golfCourseName = teeTime.getGolfCourse() != null ? teeTime.getGolfCourse().getName() : "Unknown Golf Course";

        // Extract user IDs from TeeTime
        List<Long> userIds = teeTime.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());

        // Drive to Tee Time
        Drive toTeeTime = new Drive();
        toTeeTime.setTeeTime(teeTime);
        toTeeTime.setDate(teeTime.getTeeTime().toLocalDate());
        toTeeTime.setCustomReason("Drive to Tee Time");
        toTeeTime.setDeparturePlace(hotelName);
        toTeeTime.setArrivalPlace(golfCourseName);
        toTeeTime.setUserIds(userIds); // Set user IDs
        drives.add(toTeeTime);

        // Drive from Tee Time
        Drive fromTeeTime = new Drive();
        fromTeeTime.setTeeTime(teeTime);
        fromTeeTime.setDate(teeTime.getTeeTime().toLocalDate());
        fromTeeTime.setCustomReason("Drive from Tee Time");
        fromTeeTime.setDeparturePlace(golfCourseName);
        fromTeeTime.setArrivalPlace(hotelName);
        fromTeeTime.setUserIds(userIds); // Set user IDs
        drives.add(fromTeeTime);

        return drives;
    }



    @Override
    public List<Drive> createDrivesForTransportation(TransportationReservation reservation) {
        List<Drive> drives = new ArrayList<>();

        // Extract user IDs from TransportationReservation
        List<Long> userIds = reservation.getPassengers().stream()
                .map(User::getId)
                .collect(Collectors.toList());

        // Drive to Destination
        if ("tam".equalsIgnoreCase(reservation.getRouteName())) {
            String departureAirportName = reservation.getDepartureAirportName() != null ? reservation.getDepartureAirportName() : "Unknown Departure";
            String arrivalAirportName = reservation.getArrivalAirportName() != null ? reservation.getArrivalAirportName() : "Unknown Arrival";

            Drive toDestination = new Drive();
            toDestination.setTransportationReservation(reservation);
            toDestination.setDate(reservation.getDropoffTime().toLocalDate());
            toDestination.setCustomReason("Drive to Destination");
            toDestination.setDeparturePlace(departureAirportName);
            toDestination.setArrivalPlace(arrivalAirportName);
            toDestination.setUserIds(userIds); // Set user IDs
            drives.add(toDestination);
        }

        // Drive from Destination
        if ("zpet".equalsIgnoreCase(reservation.getRouteName())) {
            String departureAirportName = reservation.getArrivalAirportName() != null ? reservation.getArrivalAirportName() : "Unknown Departure";
            String arrivalAirportName = reservation.getDepartureAirportName() != null ? reservation.getDepartureAirportName() : "Unknown Arrival";

            Drive fromDestination = new Drive();
            fromDestination.setTransportationReservation(reservation);
            fromDestination.setDate(reservation.getPickupTime().toLocalDate());
            fromDestination.setCustomReason("Drive from Destination");
            fromDestination.setDeparturePlace(departureAirportName);
            fromDestination.setArrivalPlace(arrivalAirportName);
            fromDestination.setUserIds(userIds); // Set user IDs
            drives.add(fromDestination);
        }

        return drives;
    }





    /**
     * Edit an existing drive.
     */
    @Override
    public Drive editDrive(Long driveId, LocalDateTime pickupTime, LocalDateTime dropoffTime, Driver driver) {
        Drive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new NoSuchElementException("Drive not found for ID: " + driveId));

        if (driver != null) {

            if (!driverRepository.existsById(driver.getId())) {
                throw new IllegalArgumentException("Invalid Driver ID: " + driver.getId());
            }
            drive.setDriver(driver);
        }

        drive.setPickupTime(pickupTime);
        drive.setDropoffTime(dropoffTime);

        return driveRepository.save(drive);
    }


    @Override
    public void populateMissingPlacesForDrives() {
        List<Drive> allDrives = driveRepository.findAll();

        for (Drive drive : allDrives) {
            // Populate missing places
            if (drive.getDeparturePlace() == null || drive.getArrivalPlace() == null) {
                if (drive.getTeeTime() != null) {
                    TeeTime teeTime = drive.getTeeTime();

                    // Fetch hotel name
                    String hotelName = teeTime.getUsers().stream()
                            .flatMap(user -> user.getOrderUsers().stream())
                            .filter(orderUser -> orderUser.getOrderDetail().getAccommodationReservations() != null)
                            .flatMap(orderUser -> orderUser.getOrderDetail().getAccommodationReservations().stream())
                            .map(reservation -> reservation.getObjednavkaHotel().getName())
                            .findFirst()
                            .orElse("Unknown Hotel");

                    // Fetch golf course name
                    String golfCourseName = teeTime.getGolfCourse() != null ? teeTime.getGolfCourse().getName() : "Unknown Golf Course";

                    if ("Drive to Tee Time".equals(drive.getCustomReason())) {
                        drive.setDeparturePlace(hotelName);
                        drive.setArrivalPlace(golfCourseName);
                    } else if ("Drive from Tee Time".equals(drive.getCustomReason())) {
                        drive.setDeparturePlace(golfCourseName);
                        drive.setArrivalPlace(hotelName);
                    }

                    // Populate user IDs from TeeTime
                    List<Long> userIds = teeTime.getUsers().stream()
                            .map(User::getId)
                            .collect(Collectors.toList());
                    drive.setUserIds(userIds);

                } else if (drive.getTransportationReservation() != null) {
                    TransportationReservation reservation = drive.getTransportationReservation();

                    if ("Drive to Destination".equals(drive.getCustomReason())) {
                        drive.setDeparturePlace(reservation.getDepartureAirportName());
                        drive.setArrivalPlace(reservation.getArrivalAirportName());
                    } else if ("Drive from Destination".equals(drive.getCustomReason())) {
                        drive.setDeparturePlace(reservation.getArrivalAirportName());
                        drive.setArrivalPlace(reservation.getDepartureAirportName());
                    }

                    // Populate user IDs from TransportationReservation
                    List<Long> userIds = reservation.getPassengers().stream()
                            .map(User::getId)
                            .collect(Collectors.toList());
                    drive.setUserIds(userIds);
                }
            }
        }

        // Save updated drives
        driveRepository.saveAll(allDrives);
    }




}
