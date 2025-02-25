package com.backend.service_interface;

import com.backend.dtos.Drive.DriveDTO;
import com.backend.entity.Transportation.Drive;

import java.util.List;

public interface DriveServiceInterface {
    Drive createDrive(DriveDTO driveDTO);
    Drive updateDrive(Long driveId, DriveDTO driveDTO);
    void deleteDrive(Long driveId);
    List<Drive> getUntrackedDrives();
    List<Drive> getAllDrives();
}
