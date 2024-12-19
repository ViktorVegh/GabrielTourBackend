package com.backend.controller;

import com.backend.dtos.*;
import com.backend.dtos.TeeTime.GolfCourseDTO;
import com.backend.dtos.TeeTime.TeeTimeDTO;
import com.backend.dtos.TeeTime.TeeTimeRequest;
import com.backend.entity.TeeTime.TeeTime;
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
    @PreAuthorize("hasAnyAuthority('user', 'office')")    @GetMapping("/get_tee_time")
    public List<TeeTimeDTO> getLatestTeeTimes() {
        return teeTimeService.getLatestTeeTimes();
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
    @PreAuthorize("hasAuthority('office')")
    @DeleteMapping("/delete_tee_time")
    public void deleteTeeTime(@RequestParam Long id) {
        System.out.println("Deleting a new TeeTime");
        teeTimeService.deleteTeeTime(id);
    }
    @PreAuthorize("hasAuthority('office')")
    @PutMapping("/edit_tee_time")
    public TeeTimeDTO editTeeTime(@RequestBody TeeTimeDTO teeTimeDTO) {
        System.out.println("Editing a new TeeTime");
        TeeTime updatedTeeTime = teeTimeService.editTeeTime(teeTimeDTO);
        return EntityToDTOMapper.toTeeTimeDTO(updatedTeeTime);
    }
}
