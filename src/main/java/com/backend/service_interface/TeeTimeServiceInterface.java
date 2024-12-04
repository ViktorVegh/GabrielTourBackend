package com.backend.service_interface;

import com.backend.dtos.TeeTimeDTO;
import com.backend.dtos.TeeTimeRequest;

import java.util.List;

public interface TeeTimeServiceInterface {
    List<TeeTimeDTO> getTeeTimesByUserId(Long userId);

    TeeTimeDTO createTeeTime(TeeTimeRequest teeTimeRequest);
}
