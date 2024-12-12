package com.backend.controller;

import com.backend.dtos.DriveDTO;
import com.backend.dtos.EditDriveRequest;
import com.backend.dtos.EntityToDTOMapper;
import com.backend.entity.Drive;
import com.backend.service_interface.DriveServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drives")
public class DriveController {

    @Autowired
    private DriveServiceInterface driveService;

    @PreAuthorize("hasAnyAuthority('office', 'operationmanager')")
    @GetMapping("/upcoming")
    public List<DriveDTO> getAllUntrackedDrives() {
        List<Drive> drives = driveService.getUntrackedDrives();
        return drives.stream()
                .map(EntityToDTOMapper::mapToDriveDTO)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('office', 'operationmanager')")
    @PostMapping
    public DriveDTO createDrive(@RequestBody DriveDTO driveDTO) {
        Drive createdDrive = driveService.createDrive(driveDTO);
        return EntityToDTOMapper.mapToDriveDTO(createdDrive);
    }


    @PreAuthorize("hasAnyAuthority('office', 'operationmanager')")
    @PutMapping("/{driveId}")
    public DriveDTO updateDrive(@PathVariable Long driveId, @RequestBody DriveDTO driveDTO) {
        Drive updatedDrive = driveService.updateDrive(driveId, driveDTO);
        return EntityToDTOMapper.mapToDriveDTO(updatedDrive);
    }


    @PreAuthorize("hasAnyAuthority('office', 'operationmanager')")
    @DeleteMapping("/{driveId}")
    public void deleteDrive(@PathVariable Long driveId) {
        driveService.deleteDrive(driveId);
    }

    @PreAuthorize("hasAnyAuthority('office', 'operationmanager')")
    @GetMapping("/all")
    public List<DriveDTO> getAllDrives() {
        List<Drive> drives = driveService.getAllDrives(); // New service method
        return drives.stream()
                .map(EntityToDTOMapper::mapToDriveDTO)
                .toList();
    }
}
