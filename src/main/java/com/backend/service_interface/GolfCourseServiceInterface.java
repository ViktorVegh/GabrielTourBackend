package com.backend.service_interface;

import com.backend.dtos.DriveDTO;
import com.backend.dtos.GolfCourseDTO;
import com.backend.dtos.TeeTimeDTO;
import com.backend.dtos.TeeTimeRequest;
import com.backend.entity.Drive;
import com.backend.entity.GolfCourse;

import java.util.Optional;

public interface GolfCourseServiceInterface {
    Optional<GolfCourseDTO> findGolfCourse(String name);

    GolfCourseDTO createGolfCourse(GolfCourseDTO golfCourseDTO);




}
