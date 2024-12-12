package com.backend.dtos;
import com.backend.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class EntityToDTOMapper {

    // TeeTime to TeeTimeDTO
    public static TeeTimeDTO toTeeTimeDTO(TeeTime teeTime) {
        if (teeTime == null) {
            return null;
        }

        // Convert the list of User objects to a list of user IDs
        List<Long> userIds = teeTime.getUsers() != null ?
                teeTime.getUsers().stream().map(User::getId).collect(Collectors.toList()) : null;

        return new TeeTimeDTO(
                teeTime.getId(),
                teeTime.getTeeTime(),
                teeTime.getGroupSize(),
                userIds, // Updated to pass a list of user IDs
                teeTime.getGolfCourse().getId(),                teeTime.isGreen(),
                teeTime.getHoles(),
                teeTime.getAdults(),
                teeTime.getJuniors(),
                teeTime.getNote(),
                teeTime.isNeedTransport()
        );
    }


    // GolfCourse to GolfCourseDTO
    public static GolfCourseDTO toGolfCourseDTO(GolfCourse golfCourse) {
        if (golfCourse == null) {
            return null;
        }

        return new GolfCourseDTO(
                golfCourse.getId(),
                golfCourse.getName()
        );
    }


    // Hotel to HotelDTO
    public static HotelDTO toHotelDTO(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        return new HotelDTO(
                hotel.getExternalValues(),
                hotel.getStars(),
                hotel.getId(),
                hotel.getName(),
                hotel.getRegion(),
                hotel.getCountry(),
                hotel.getArea()
        );
    }

    public static PersonDTO mapToPersonDTO(Person person) {
        if (person == null) {
            return null;
        }

        return new PersonDTO(
                person.getId(),
                person.getEmail(),
                person.getName().orElse(null),
                person.getRole(),
                person.getProfilePicture().orElse(null)
        );
    }
    public static GolfCourseDTO mapToGolfCourseDTO(GolfCourse golfCourse) {
        if (golfCourse == null) {
            return null;
        }

        return new GolfCourseDTO(
                golfCourse.getId(),
                golfCourse.getName()
        );
    }

    public static DriveDTO mapToDriveDTO(Drive drive) {
        if (drive == null) {
            return null;
        }

        return new DriveDTO(
                drive.getId(),
                drive.getDate(),
                drive.getPickupTime(),
                drive.getDropoffTime(),
                drive.getCustomReason(),
                drive.getDriver() != null ? drive.getDriver().getId() : null,
                drive.getDeparturePlace(),
                drive.getArrivalPlace(),
                drive.getUserIds()
        );
    }


    public static Drive mapToDrive(DriveDTO driveDTO, Driver driver) {
        Drive drive = new Drive();
        drive.setId(driveDTO.getId());
        drive.setDate(driveDTO.getDate());
        drive.setPickupTime(driveDTO.getPickupTime());
        drive.setDropoffTime(driveDTO.getDropoffTime());
        drive.setCustomReason(driveDTO.getCustomReason());
        drive.setDeparturePlace(driveDTO.getDeparturePlace());
        drive.setArrivalPlace(driveDTO.getArrivalPlace());
        drive.setUserIds(driveDTO.getUserIds());
        drive.setDriver(driver);
        return drive;
    }


    public static OrderDTO mapToOrderDTO(OrderDetail detail) {
        if (detail == null) {
            return null;
        }
        return new OrderDTO(
                new OrderDetail(
                        detail.getId(),
                        detail.getTotalPrice(),
                        detail.getPricing(),
                        detail.getStartDate(),
                        detail.getEndDate(),
                        detail.getNumberOfDays(),
                        detail.getNumberOfNights(),
                        detail.getAdults(),
                        detail.getChildren(),
                        detail.getInfants(),
                        detail.getCurrency(),
                        detail.getPaymentStatus(),
                        detail.getName(),
                        detail.getExternalValues(),
                        detail.getAccommodationReservations(),
                        detail.getTransportationReservations(),
                        detail.getPrices(),
                        detail.getTravelers(),
                        detail.getTermId(),
                        detail.getStateOfOrder()
                )
        );
    }
}
