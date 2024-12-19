package com.backend.service_interface;

import com.backend.entity.Transportation.DrivesCalendar;

import java.util.List;

public interface DriveScheduleServiceInterface {
    DrivesCalendar initializeCalendar();
    DrivesCalendar getDrivesCalendar();
    void removeDrivesFromCalendar(List<Long> driveIds);
    void addDriveToCalendar(Long driveId);
}

