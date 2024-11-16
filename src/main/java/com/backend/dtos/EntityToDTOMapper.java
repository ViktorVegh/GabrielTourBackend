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
                teeTime.getNote()
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

    // Accommodation to AccommodationDTO
    public static AccommodationDTO toAccommodationDTO(Accommodation accommodation) {
        if (accommodation == null) {
            return null;
        }

        return new AccommodationDTO(
                accommodation.getStartDate(),
                accommodation.getReservationId(),
                accommodation.getBeds(),
                accommodation.getAccommodationName(),
                accommodation.getNights(),
                accommodation.getNotes(),
                accommodation.getExtraBeds(),
                accommodation.getMealPlan(),
                toHotelDTO(accommodation.getHotelDetails()),
                accommodation.getAccommodationTravelers() != null ?
                        accommodation.getAccommodationTravelers().stream().map(User::getId).collect(Collectors.toList()) : null
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
                hotel.hasHalfStar(),
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
}
