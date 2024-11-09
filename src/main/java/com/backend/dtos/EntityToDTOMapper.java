package com.backend.dtos;

import com.backend.entity.GolfCourse;
import com.backend.entity.Person;
import com.backend.entity.Resort;
import com.backend.entity.TeeTime;

public class EntityToDTOMapper {

    public static TeeTimeDTO mapToTeeTimeDTO(TeeTime teeTime) {
        if (teeTime == null) {
            return null;
        }

        GolfCourseDTO golfCourseDTO = mapToGolfCourseDTO(teeTime.getGolfCourse());

        return new TeeTimeDTO(
                teeTime.getId(),
                teeTime.getTeeTime(),
                teeTime.getGroupSize(),
                teeTime.getUser() != null ? teeTime.getUser().getId() : null, // Include userId
                golfCourseDTO
        );
    }

    public static GolfCourseDTO mapToGolfCourseDTO(GolfCourse golfCourse) {
        if (golfCourse == null) {
            return null;
        }

        ResortDTO resortDTO = golfCourse.getResort() != null ? mapToResortDTO(golfCourse.getResort()) : null;

        return new GolfCourseDTO(
                golfCourse.getId(),
                golfCourse.getName(),
                resortDTO
        );
    }

    public static ResortDTO mapToResortDTO(Resort resort) {
        if (resort == null) {
            return null;
        }

        return new ResortDTO(
                resort.getId(),
                resort.getName(),
                resort.getAddress(),
                resort.getCountry()
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
