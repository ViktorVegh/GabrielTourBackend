package com.backend.service_interface;

import com.backend.dtos.TeeTime.GolfCourseDTO;

import java.util.Optional;

public interface GolfCourseServiceInterface {
    Optional<GolfCourseDTO> findGolfCourse(String name);

    GolfCourseDTO createGolfCourse(GolfCourseDTO golfCourseDTO);




}
