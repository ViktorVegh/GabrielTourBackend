package com.backend.service;

import com.backend.entity.Transportation.Drive;
import com.backend.entity.Transportation.DrivesCalendar;
import com.backend.repository.DriveRepository;
import com.backend.repository.DrivesCalendarRepository;
import com.backend.service_interface.DriveScheduleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DriveScheduleService implements DriveScheduleServiceInterface {

    @Autowired
    private DrivesCalendarRepository drivesCalendarRepository;

    @Autowired
    private DriveRepository driveRepository;

    private static final Long CALENDAR_ID = 1L;

    @Override
    public DrivesCalendar initializeCalendar() {
        DrivesCalendar calendar = new DrivesCalendar();
        calendar.setId(CALENDAR_ID); // Set the fixed ID for the single calendar
        calendar.setMonthStartDate(LocalDate.now().withDayOfMonth(1)); // Start of the current month
        calendar.setMonthEndDate(YearMonth.now().atEndOfMonth()); // End of the current month
        return drivesCalendarRepository.save(calendar);
    }



    @Override
    public DrivesCalendar getDrivesCalendar() {
        DrivesCalendar calendar = drivesCalendarRepository.findById(CALENDAR_ID)
                .orElseGet(this::initializeCalendar);

        // Ensure the calendar dates are correctly set for the current range
        updateCalendarDatesIfNecessary(calendar);

        // Remove expired drives before returning the calendar
        removeExpiredDrivesFromCalendar(calendar);

        return calendar;
    }


    @Override
    public void removeDrivesFromCalendar(List<Long> driveIds) {
        DrivesCalendar calendar = drivesCalendarRepository.findById(CALENDAR_ID)
                .orElseThrow(() -> new IllegalStateException("DrivesCalendar not found. Please initialize it."));

        // Remove drives with matching IDs from the calendar
        calendar.getDrives().removeIf(drive -> driveIds.contains(drive.getId()));

        drivesCalendarRepository.save(calendar);
    }



    private void removeExpiredDrivesFromCalendar(DrivesCalendar calendar) {
        LocalDate today = LocalDate.now();

        // Remove drives whose dates are before the current date
        calendar.getDrives().removeIf(drive -> drive.getDate().isBefore(today));

        drivesCalendarRepository.save(calendar);
    }


    private void updateCalendarDatesIfNecessary(DrivesCalendar calendar) {
        LocalDate today = LocalDate.now();
        LocalDate expectedStartDate = today;
        LocalDate expectedEndDate = today.plusMonths(1).withDayOfMonth(1).minusDays(1); // Next month

        if (!calendar.getMonthStartDate().equals(expectedStartDate) || !calendar.getMonthEndDate().equals(expectedEndDate)) {
            // Update calendar dates
            calendar.setMonthStartDate(expectedStartDate);
            calendar.setMonthEndDate(expectedEndDate);

            // Remove drives outside the new range (but keep expired ones intact)
            calendar.getDrives().removeIf(drive ->
                    drive.getDate().isBefore(expectedStartDate) || drive.getDate().isAfter(expectedEndDate)
            );

            // Save updated calendar
            drivesCalendarRepository.save(calendar);
        }



    }


//    @Override
//    public void addDriveToCalendar(Long driveId) {
//        Drive drive = driveRepository.findById(driveId)
//                .orElseThrow(() -> new NoSuchElementException("Drive not found for ID: " + driveId));
//
//        if (!isDriveComplete(drive)) {
//            throw new IllegalArgumentException("Drive is not complete and cannot be added to the calendar.");
//        }
//
//        LocalDate currentDate = LocalDate.now();
//        if (!drive.getDate().isAfter(currentDate)) { // Check if drive date is after current date
//            throw new IllegalArgumentException("Cannot add a past or today's drive to the calendar.");
//        }
//
//        DrivesCalendar calendar = drivesCalendarRepository.findById(CALENDAR_ID)
//                .orElseThrow(() -> new IllegalStateException("DrivesCalendar not found. Please initialize it."));
//
//        if (!calendar.getDrives().contains(drive)) {
//            calendar.getDrives().add(drive);
//            drivesCalendarRepository.save(calendar);
//        }
//    }

    @Override
    public void addDriveToCalendar(Long driveId) {
        System.out.println("Adding drive to calendar with ID: " + driveId);

        Drive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> {
                    System.out.println("Drive not found for ID: " + driveId);
                    return new NoSuchElementException("Drive not found for ID: " + driveId);
                });
        System.out.println("Fetched drive: " + drive);

        if (!isDriveComplete(drive)) {
            System.out.println("Drive is incomplete: " + drive);
            throw new IllegalArgumentException("Drive is not complete and cannot be added to the calendar.");
        }
        System.out.println("Drive is complete.");

        LocalDate currentDate = LocalDate.now();
        System.out.println("Current date: " + currentDate + ", Drive date: " + drive.getDate());
        if (!drive.getDate().isAfter(currentDate)) {
            System.out.println("Drive date is not valid for the calendar.");
            throw new IllegalArgumentException("Cannot add a past or today's drive to the calendar.");
        }

        DrivesCalendar calendar = drivesCalendarRepository.findById(CALENDAR_ID)
                .orElseThrow(() -> {
                    System.out.println("Calendar not found for ID: " + CALENDAR_ID);
                    return new IllegalStateException("DrivesCalendar not found. Please initialize it.");
                });
        System.out.println("Fetched calendar: " + calendar);

        if (!calendar.getDrives().contains(drive)) {
            System.out.println("Drive not already in calendar. Adding drive to calendar.");
            calendar.getDrives().add(drive);
            drivesCalendarRepository.save(calendar);
            System.out.println("Drive added to calendar successfully.");
        } else {
            System.out.println("Drive already exists in the calendar. Skipping addition.");
        }
    }



    private boolean isDriveComplete(Drive drive) {
        return drive.getPickupTime() != null
                && drive.getDropoffTime() != null
                && drive.getDriver() != null
                && drive.getDeparturePlace() != null
                && drive.getArrivalPlace() != null;
    }



}


