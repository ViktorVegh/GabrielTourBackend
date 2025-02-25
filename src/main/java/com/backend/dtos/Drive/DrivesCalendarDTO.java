package com.backend.dtos.Drive;

import java.time.LocalDate;
import java.util.List;

public class DrivesCalendarDTO {
    private Long calendarId;
    private LocalDate monthStartDate;
    private LocalDate monthEndDate;
    private List<DriveDTO> drives;

    public DrivesCalendarDTO(Long calendarId, LocalDate monthStartDate, LocalDate monthEndDate, List<DriveDTO> drives) {
        this.calendarId = calendarId;
        this.monthStartDate = monthStartDate;
        this.monthEndDate = monthEndDate;
        this.drives = drives;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public LocalDate getMonthStartDate() {
        return monthStartDate;
    }

    public void setMonthStartDate(LocalDate monthStartDate) {
        this.monthStartDate = monthStartDate;
    }

    public LocalDate getMonthEndDate() {
        return monthEndDate;
    }

    public void setMonthEndDate(LocalDate monthEndDate) {
        this.monthEndDate = monthEndDate;
    }

    public List<DriveDTO> getDrives() {
        return drives;
    }

    public void setDrives(List<DriveDTO> drives) {
        this.drives = drives;
    }
}
