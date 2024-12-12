package com.backend.service_interface;

import com.backend.entity.Drive;
import com.backend.entity.DrivesCalendar;

import java.util.List;

public interface DriveScheduleServiceInterface {
    DrivesCalendar initializeCalendar();
    List<Drive> getMonthlyCalendar();
    void removeDrivesFromCalendar(List<Long> driveIds);
    void addDriveToCalendar(Long driveId);
}

