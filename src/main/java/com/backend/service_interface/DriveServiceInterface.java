package com.backend.service_interface;

import com.backend.dtos.DriveDTO;
import com.backend.entity.Driver;
import com.backend.entity.Drive;

import java.time.LocalDateTime;
import java.util.List;

public interface DriveServiceInterface {
    Drive createDrive(DriveDTO driveDTO);
    Drive updateDrive(Long driveId, DriveDTO driveDTO);
    void deleteDrive(Long driveId);
    List<Drive> getUntrackedDrives();
    List<Drive> getAllDrives();
}
