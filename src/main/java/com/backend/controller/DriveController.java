package com.backend.controller;

import com.backend.dtos.DriveDTO;
import com.backend.dtos.EditDriveRequest;
import com.backend.entity.Drive;
import com.backend.service.DriveScheduleService;
import com.backend.service.DriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.backend.dtos.EntityToDTOMapper;

import java.util.List;
@RestController
@RequestMapping("/api/drives")
public class DriveController {

    @Autowired
    private DriveScheduleService driveScheduleService;

    @Autowired
    private DriveService driveService;

    /**
     * Updates the calendar for the current week and returns all drives.
     */
    @PreAuthorize("hasAnyAuthority('driver', 'office', 'operationmanager')")
    @GetMapping("/current-week")
    public List<DriveDTO> getAndUpdateCurrentWeekDrives() {
        driveScheduleService.updateWeeklyCalendar();
        List<Drive> drives = driveScheduleService.getWeeklyCalendar();
        return drives.stream()
                .map(EntityToDTOMapper::mapToDriveDTO)
                .toList();
    }


    @PreAuthorize("hasAnyAuthority('office', 'operationmanager')")
    @PutMapping("/{driveId}/edit")
    public DriveDTO editDrive(
            @PathVariable Long driveId,
            @RequestBody EditDriveRequest request
    ) {
        Drive updatedDrive = driveService.editDrive(
                driveId,
                request.getPickupTime(),
                request.getDropoffTime(),
                request.getDriver()
        );

        return EntityToDTOMapper.mapToDriveDTO(updatedDrive);
    }

    @PreAuthorize("hasAnyAuthority('office', 'operationmanager')")
    @PostMapping("/update-missing-places")
    public void updateMissingPlacesForDrives() {
        driveService.populateMissingPlacesForDrives();
    }

}
