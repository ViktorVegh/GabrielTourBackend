package com.backend.controller;

import com.backend.dtos.DriveDTO;
import com.backend.dtos.EntityToDTOMapper;
import com.backend.entity.Drive;
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

    @PreAuthorize("hasAnyAuthority('office', 'operationmanager','driver')")
    @GetMapping("/monthly")
    public List<DriveDTO> getMonthlyCalendar() {
        List<Drive> drives = driveScheduleService.getMonthlyCalendar();
        return drives.stream()
                .map(EntityToDTOMapper::mapToDriveDTO)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('office', 'operationmanager')")
    @DeleteMapping("/remove")
    public void removeDrivesFromCalendar(@RequestBody List<Long> driveIds) {
        driveScheduleService.removeDrivesFromCalendar(driveIds);
    }

    @PreAuthorize("hasAnyAuthority('office', 'operationmanager')")
    @PostMapping("/add")
    public void addDriveToCalendar(@RequestParam Long driveId) {
        driveScheduleService.addDriveToCalendar(driveId);
    }
}
