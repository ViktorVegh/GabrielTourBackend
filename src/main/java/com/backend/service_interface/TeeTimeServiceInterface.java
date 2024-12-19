package com.backend.service_interface;

import com.backend.dtos.TeeTime.TeeTimeDTO;
import com.backend.dtos.TeeTime.TeeTimeRequest;
import com.backend.entity.TeeTime.TeeTime;

import java.util.List;

public interface TeeTimeServiceInterface {
    List<TeeTimeDTO> getTeeTimesByUserId(Long userId);
    List<TeeTimeDTO> getLatestTeeTimes();

    TeeTimeDTO createTeeTime(TeeTimeRequest teeTimeRequest);

    TeeTime editTeeTime(TeeTimeDTO teeTimeDTO);
    void deleteTeeTime(Long id);
}
