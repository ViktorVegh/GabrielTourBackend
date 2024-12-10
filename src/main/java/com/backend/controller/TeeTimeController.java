package com.backend.controller;

import com.backend.dtos.GolfCourseDTO;
import com.backend.dtos.PersonDTO;
import com.backend.dtos.TeeTimeDTO;
import com.backend.dtos.TeeTimeRequest;
import com.backend.service.TeeTimeService;
import com.backend.service_interface.GolfCourseServiceInterface;
import com.backend.service_interface.TeeTimeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teetimes")
public class TeeTimeController {

    @Autowired
    private TeeTimeServiceInterface teeTimeService;

    @Autowired
    private GolfCourseServiceInterface golfCourseService;

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
    @PreAuthorize("hasAuthority('office')")
    @GetMapping("/get_golf_course")
    public ResponseEntity<GolfCourseDTO> searchGolfCourse(@RequestParam String name) {
        return golfCourseService.findGolfCourse(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasAuthority('office')")
    @PostMapping("/create_golf_course")
    public GolfCourseDTO createGolfCourse(@RequestBody GolfCourseDTO golfCourseDTO) {
        System.out.println("Creating a new TeeTime");
        return golfCourseService.createGolfCourse(golfCourseDTO);
    }
}
