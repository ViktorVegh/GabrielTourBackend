package com.backend.service_interface;

import com.backend.entity.Drive;
import com.backend.entity.Driver;
import com.backend.entity.TeeTime;
import com.backend.entity.TransportationReservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DriveServiceInterface {
    List<Drive> getDrivesForDateRange(LocalDate startDate, LocalDate endDate);

    List<Drive> createDrivesForTeeTime(TeeTime teeTime);

    List<Drive> createDrivesForTransportation(TransportationReservation reservation);

    Drive editDrive(Long driveId, LocalDateTime pickupTime, LocalDateTime dropoffTime, Driver driver);

    void populateMissingPlacesForDrives();
}
