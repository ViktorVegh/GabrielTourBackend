package com.backend.dtos;
import com.backend.dtos.Acommodation.HotelDTO;
import com.backend.dtos.Drive.DriveDTO;
import com.backend.dtos.Drive.DrivesCalendarDTO;
import com.backend.dtos.Order.OrderDTO;
import com.backend.dtos.Person.PersonDTO;
import com.backend.dtos.TeeTime.GolfCourseDTO;
import com.backend.dtos.TeeTime.TeeTimeDTO;
import com.backend.entity.Acommodation.Hotel;
import com.backend.entity.Order.OrderDetail;
import com.backend.entity.Person.Driver;
import com.backend.entity.Person.Person;
import com.backend.entity.Person.User;
import com.backend.entity.TeeTime.GolfCourse;
import com.backend.entity.TeeTime.TeeTime;
import com.backend.entity.Transportation.Drive;
import com.backend.entity.Transportation.DrivesCalendar;

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
                teeTime.getUsers().stream().map(User::getProfisId).collect(Collectors.toList()) : null;

        GolfCourse golfCourse = teeTime.getGolfCourse();
        Long golfCourseId = (golfCourse != null) ? golfCourse.getId() : null;
        String golfCourseName = (golfCourse != null) ? golfCourse.getName() : null;

        return new TeeTimeDTO(
                teeTime.getId(),
                teeTime.getTeeTime(),
                teeTime.getGroupSize(),
                userIds, // Updated to pass a list of user IDs
                golfCourseId,
                golfCourseName,
                teeTime.isGreen(),
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

    public static Drive mapToDrive(DriveDTO driveDTO, Driver driver) {

        Drive drive = new Drive();
        drive.setId(driveDTO.getId());
        drive.setDate(driveDTO.getDate());
        drive.setPickupTime(driveDTO.getPickupTime());
        drive.setDropoffTime(driveDTO.getDropoffTime());
        drive.setCustomReason(driveDTO.getCustomReason());
        drive.setDeparturePlace(driveDTO.getDeparturePlace());
        drive.setArrivalPlace(driveDTO.getArrivalPlace());

        // Deduplicate userIds
        List<Long> uniqueUserIds = driveDTO.getUserIds().stream()
                .distinct()
                .collect(Collectors.toList());
        drive.setUserIds(uniqueUserIds);

        drive.setDriver(driver);
        return drive;
    }

    public static DriveDTO mapToDriveDTO(Drive drive) {
        if (drive == null) {
            return null;
        }

        // Deduplicate userIds in the DTO mapping
        List<Long> uniqueUserIds = drive.getUserIds().stream()
                .distinct()
                .collect(Collectors.toList());

        return new DriveDTO(
                drive.getId(),
                drive.getDate(),
                drive.getPickupTime(),
                drive.getDropoffTime(),
                drive.getCustomReason(),
                drive.getDriver() != null ? drive.getDriver().getId() : null,
                drive.getDeparturePlace(),
                drive.getArrivalPlace(),
                uniqueUserIds
        );
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

    public static DrivesCalendarDTO mapToDrivesCalendarDTO(DrivesCalendar calendar) {
        if (calendar == null) {
            return null;
        }

        // Map the drives in the calendar
        List<DriveDTO> driveDTOs = calendar.getDrives().stream()
                .map(EntityToDTOMapper::mapToDriveDTO)
                .collect(Collectors.toList());

        return new DrivesCalendarDTO(
                calendar.getId(),
                calendar.getMonthStartDate(),
                calendar.getMonthEndDate(),
                driveDTOs
        );
    }

}
