package com.backend.service_interface;

import com.backend.dtos.GolfCourseDTO;
import com.backend.dtos.TeeTimeDTO;
import com.backend.dtos.TeeTimeRequest;
import com.backend.entity.GolfCourse;
import com.backend.entity.TeeTime;

import java.util.List;

public interface TeeTimeServiceInterface {
    List<TeeTimeDTO> getTeeTimesByUserId(Long userId);
    List<TeeTimeDTO> getLatestTeeTimes();

    TeeTimeDTO createTeeTime(TeeTimeRequest teeTimeRequest);

    TeeTime editTeeTime(TeeTimeDTO teeTimeDTO);
    void deleteTeeTime(Long id);
}
