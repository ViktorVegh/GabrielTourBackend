package com.backend.controller;

import com.backend.dtos.TeeTimeRequest;
import com.backend.entity.GolfCourse;
import com.backend.entity.TeeTime;
import com.backend.profis_service.TeeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/teetimes")
public class TeeTimeController {

    @Autowired
    private TeeTimeService teeTimeService;

    // Endpoint to get all tee times for a specific user by user ID
    @GetMapping("/user/{userId}")
    public List<TeeTime> getTeeTimesByUserId(@PathVariable Long userId) {
        return teeTimeService.getTeeTimesByUserId(userId);
    }

    @PreAuthorize("hasAuthority('office')")
    @PostMapping("/create")
    public TeeTime createTeeTime(@RequestBody TeeTimeRequest teeTimeRequest) {
        System.out.println("i got here");
        return teeTimeService.createTeeTime(
                teeTimeRequest.getUserId(),
                teeTimeRequest.getGolfCourse(),
                teeTimeRequest.getTeeTime(),
                teeTimeRequest.getGroupSize()
        );
    }
}
