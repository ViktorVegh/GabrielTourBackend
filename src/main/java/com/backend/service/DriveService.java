package com.backend.service;

import com.backend.dtos.DriveDTO;
import com.backend.entity.*;
import com.backend.repository.*;
import com.backend.service_interface.DriveServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.dtos.EntityToDTOMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.backend.dtos.EntityToDTOMapper.mapToDrive;

@Service
public class DriveService implements DriveServiceInterface {

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DrivesCalendarRepository drivesCalendarRepository;

    @Autowired
    private TeeTimeRepository teeTimeRepository;

    @Autowired
    private TransportationReservationRepository transportationReservationRepository;




    public Drive createDrive(DriveDTO driveDTO) {
        Driver driver = null;

        // Check if driverId is provided and fetch the Driver entity
        if (driveDTO.getDriverId() != null) {
            driver = driverRepository.findById(driveDTO.getDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + driveDTO.getDriverId()));
        }

        // Map the DTO to the Drive entity, passing the driver (null if not provided)
        Drive drive = EntityToDTOMapper.mapToDrive(driveDTO, driver);

        // Save and return the Drive entity
        return driveRepository.save(drive);
    }


    @Override
    public Drive updateDrive(Long driveId, DriveDTO driveDTO) {
        // Fetch the existing Drive entity
        Drive existingDrive = driveRepository.findById(driveId)
                .orElseThrow(() -> new NoSuchElementException("Drive not found for ID: " + driveId));

        // Map the DTO to a new Drive entity for comparison
        Driver driver = null;
        if (driveDTO.getDriverId() != null) {
            driver = driverRepository.findById(driveDTO.getDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Driver ID: " + driveDTO.getDriverId()));
        }

        Drive newDrive = EntityToDTOMapper.mapToDrive(driveDTO, driver);

        // Check if the existing Drive needs to be updated
        if (updateDriveIfNecessary(existingDrive, newDrive)) {
            return driveRepository.save(existingDrive); // Save only if updated
        }

        // If no updates were necessary, return the existing drive
        return existingDrive;
    }



    @Override
    public void deleteDrive(Long driveId) {
        if (!driveRepository.existsById(driveId)) {
            throw new NoSuchElementException("Drive not found for ID: " + driveId);
        }
        driveRepository.deleteById(driveId);
    }

    @Override
    public List<Drive> getUntrackedDrives() {
        LocalDate currentDate = LocalDate.now();

        // Get all drives from the repository
        List<Drive> allDrives = driveRepository.findAll();

        // Fetch all TeeTimes and TransportationReservations
        List<TeeTime> teeTimes = teeTimeRepository.findAll();
        List<TransportationReservation> reservations = transportationReservationRepository.findAll();

        List<Drive> untrackedDrives = new ArrayList<>();

        // Process TeeTimes and create/update drives
        for (TeeTime teeTime : teeTimes) {
            if (!teeTime.isNeedTransport()) {
                continue; // Skip tee times that do not require transportation
            }

            List<Drive> newDrives = TeeTimeDriveFactory.createDrivesForTeeTime(teeTime);

            for (Drive newDrive : newDrives) {
                Optional<Drive> existingDriveOpt = driveRepository.findByDateAndCustomReasonAndTeeTime(
                        newDrive.getDate(),
                        newDrive.getCustomReason(),
                        teeTime
                );

                if (existingDriveOpt.isPresent()) {
                    Drive existingDrive = existingDriveOpt.get();

                    // Use the reusable method to update the drive if necessary
                    if (updateDriveIfNecessary(existingDrive, newDrive)) {
                        Drive updatedDrive = driveRepository.save(existingDrive); // Save changes
                        untrackedDrives.add(updatedDrive); // Add to the result list
                    }
                } else if (newDrive.getDate().isAfter(currentDate)) {
                    // If drive doesn't exist, save it
                    Drive savedDrive = driveRepository.save(newDrive);
                    untrackedDrives.add(savedDrive);
                }
            }
        }

        // Process TransportationReservations and create/update drives
        for (TransportationReservation reservation : reservations) {
            List<Drive> newDrives = TransportationDriveFactory.createDrivesForTransportation(reservation);

            for (Drive newDrive : newDrives) {
                Optional<Drive> existingDriveOpt = driveRepository.findByDateAndCustomReasonAndTransportationReservation(
                        newDrive.getDate(),
                        newDrive.getCustomReason(),
                        reservation
                );

                if (existingDriveOpt.isPresent()) {
                    Drive existingDrive = existingDriveOpt.get();

                    // Use the reusable method to update the drive if necessary
                    if (updateDriveIfNecessary(existingDrive, newDrive)) {
                        Drive updatedDrive = driveRepository.save(existingDrive); // Save changes
                        untrackedDrives.add(updatedDrive); // Add to the result list
                    }
                } else if (newDrive.getDate().isAfter(currentDate)) {
                    // If drive doesn't exist, save it
                    Drive savedDrive = driveRepository.save(newDrive);
                    untrackedDrives.add(savedDrive);
                }
            }
        }

        return untrackedDrives;
    }




    @Override
    public List<Drive> getAllDrives() {
        return driveRepository.findAll();
    }


    private boolean updateDriveIfNecessary(Drive existingDrive, Drive newDrive) {
        boolean isUpdated = false;

        if (!areEqual(existingDrive.getUserIds(), newDrive.getUserIds())) {
            existingDrive.setUserIds(newDrive.getUserIds());
            isUpdated = true;
        }
        if (!areEqual(existingDrive.getDeparturePlace(), newDrive.getDeparturePlace())) {
            existingDrive.setDeparturePlace(newDrive.getDeparturePlace());
            isUpdated = true;
        }
        if (!areEqual(existingDrive.getArrivalPlace(), newDrive.getArrivalPlace())) {
            existingDrive.setArrivalPlace(newDrive.getArrivalPlace());
            isUpdated = true;
        }
        if (!areEqual(existingDrive.getCustomReason(), newDrive.getCustomReason())) {
            existingDrive.setCustomReason(newDrive.getCustomReason());
            isUpdated = true;
        }
        if (!areEqual(existingDrive.getPickupTime(), newDrive.getPickupTime())) {
            existingDrive.setPickupTime(newDrive.getPickupTime());
            isUpdated = true;
        }
        if (!areEqual(existingDrive.getDropoffTime(), newDrive.getDropoffTime())) {
            existingDrive.setDropoffTime(newDrive.getDropoffTime());
            isUpdated = true;
        }
        if (!areEqual(existingDrive.getDate(), newDrive.getDate())) {
            existingDrive.setDate(newDrive.getDate());
            isUpdated = true;
        }
        if (!areEqual(existingDrive.getDriver(), newDrive.getDriver())) {
            existingDrive.setDriver(newDrive.getDriver());
            isUpdated = true;
        }
        if (!areEqual(existingDrive.getTransportationReservation(), newDrive.getTransportationReservation())) {
            existingDrive.setTransportationReservation(newDrive.getTransportationReservation());
            isUpdated = true;
        }

        return isUpdated;
    }

    private boolean areEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }




}

