package com.backend.controller;

import com.backend.dtos.Drive.DrivesCalendarDTO;
import com.backend.dtos.EntityToDTOMapper;
import com.backend.entity.Transportation.DrivesCalendar;
import com.backend.service_interface.DriveScheduleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drives-calendar")
public class DrivesCalendarController {

    @Autowired
    private DriveScheduleServiceInterface driveScheduleService;

    @PreAuthorize("hasAnyAuthority('office', 'drivermanager','driver','user')")
    @GetMapping("/monthly")
    public DrivesCalendarDTO getMonthlyCalendar() {
        DrivesCalendar calendar = driveScheduleService.getDrivesCalendar();
        return EntityToDTOMapper.mapToDrivesCalendarDTO(calendar);
    }


    @PreAuthorize("hasAnyAuthority('office', 'drivermanager')")
    @DeleteMapping("/remove")
    public void removeDrivesFromCalendar(@RequestBody List<Long> driveIds) {
        driveScheduleService.removeDrivesFromCalendar(driveIds);
    }

    @PreAuthorize("hasAnyAuthority('office', 'drivermanager')")
    @PostMapping("/add")
    public void addDriveToCalendar(@RequestParam Long driveId) {
        driveScheduleService.addDriveToCalendar(driveId);
    }
}
