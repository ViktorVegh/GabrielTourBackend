package com.backend.controller;

import com.backend.dtos.TeeTimeDTO;
import com.backend.dtos.TeeTimeRequest;
import com.backend.service.TeeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teetimes")
public class TeeTimeController {

    @Autowired
    private TeeTimeService teeTimeService;

    @PreAuthorize("hasAnyAuthority('user', 'office')")    @GetMapping("/user/{userId}")
    public List<TeeTimeDTO> getTeeTimesByUserId(@PathVariable Long userId) {
        return teeTimeService.getTeeTimesByUserId(userId);
    }

    @PreAuthorize("hasAuthority('office')")
    @PostMapping("/create")
    public TeeTimeDTO createTeeTime(@RequestBody TeeTimeRequest teeTimeRequest) {
        System.out.println("Creating a new TeeTime");
        return teeTimeService.createTeeTime(teeTimeRequest);
    }
}
