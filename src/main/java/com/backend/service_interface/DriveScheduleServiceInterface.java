package com.backend.service_interface;

import com.backend.entity.Drive;

import java.util.List;

public interface DriveScheduleServiceInterface {
    void updateWeeklyCalendar();

    List<Drive> getWeeklyCalendar();

    boolean isCalendarCurrent();
}
