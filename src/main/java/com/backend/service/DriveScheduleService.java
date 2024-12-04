package com.backend.service;

import com.backend.entity.Drive;
import com.backend.service_interface.DriveScheduleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriveScheduleService implements DriveScheduleServiceInterface {

    @Autowired
    private DriveService driveService;

    private List<Drive> weeklyCalendar = new ArrayList<>();

    @Override
    public void updateWeeklyCalendar() {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(6);

        weeklyCalendar = driveService.getDrivesForDateRange(today, endOfWeek);
    }

    @Override
    public List<Drive> getWeeklyCalendar() {
        if (weeklyCalendar.isEmpty() || !isCalendarCurrent()) {
            updateWeeklyCalendar();
        }
        return weeklyCalendar;
    }

    @Override
    public boolean isCalendarCurrent() {
        LocalDate today = LocalDate.now();
        return !weeklyCalendar.isEmpty() &&
                weeklyCalendar.stream().allMatch(d -> d.getDate() != null && !d.getDate().isBefore(today));
    }
}
