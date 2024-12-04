package com.backend.service;

import com.backend.entity.Drive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriveScheduleService {

    @Autowired
    private DriveService driveService;

    private List<Drive> weeklyCalendar = new ArrayList<>();

    public void updateWeeklyCalendar() {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(6);

        weeklyCalendar = driveService.getDrivesForDateRange(today, endOfWeek);
    }

    public List<Drive> getWeeklyCalendar() {
        if (weeklyCalendar.isEmpty() || !isCalendarCurrent()) {
            updateWeeklyCalendar();
        }
        return weeklyCalendar;
    }

    private boolean isCalendarCurrent() {
        LocalDate today = LocalDate.now();
        return !weeklyCalendar.isEmpty() &&
                weeklyCalendar.stream().allMatch(d -> d.getDate() != null && !d.getDate().isBefore(today));
    }
}
