package com.backend.service;

import com.backend.dtos.Drive.DriveDTO;
import com.backend.entity.Person.Driver;
import com.backend.entity.TeeTime.TeeTime;
import com.backend.entity.Transportation.Drive;
import com.backend.entity.Transportation.TransportationReservation;
import com.backend.repository.*;
import com.backend.service_interface.DriveServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.dtos.EntityToDTOMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DriveService implements DriveServiceInterface {

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriveScheduleService driveScheduleService;


    @Autowired
    private TeeTimeRepository teeTimeRepository;

    @Autowired
    private TransportationReservationRepository transportationReservationRepository;

    @Autowired
    private TeeTimeDriveFactory teeTimeDriveFactory;




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


//    @Override
//    public Drive updateDrive(Long driveId, DriveDTO driveDTO) {
//        // Fetch the existing Drive entity
//        Drive existingDrive = driveRepository.findById(driveId)
//                .orElseThrow(() -> new NoSuchElementException("Drive not found for ID: " + driveId));
//
//        // Map the DTO to a new Drive entity for comparison
//        Driver driver = null;
//        if (driveDTO.getDriverId() != null) {
//            driver = driverRepository.findById(driveDTO.getDriverId())
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid Driver ID: " + driveDTO.getDriverId()));
//        }
//
//        Drive newDrive = EntityToDTOMapper.mapToDrive(driveDTO, driver);
//
//        // Check if the existing Drive needs to be updated
//        if (updateDriveIfNecessary(existingDrive, newDrive)) {
//            return driveRepository.save(existingDrive); // Save only if updated
//        }
//
//        driveScheduleService.addDriveToCalendar(driveId);
//        // If no updates were necessary, return the existing drive
//        return existingDrive;
//    }

    @Override
    public Drive updateDrive(Long driveId, DriveDTO driveDTO) {
        System.out.println("Updating drive with ID: " + driveId);
        System.out.println("Received DTO: " + driveDTO);

        // Fetch the existing Drive entity
        Drive existingDrive = driveRepository.findById(driveId)
                .orElseThrow(() -> {
                    System.out.println("Drive not found for ID: " + driveId);
                    return new NoSuchElementException("Drive not found for ID: " + driveId);
                });
        System.out.println("Fetched existing drive: " + existingDrive);

        // Map the DTO to a new Drive entity for comparison
        Driver driver = null;
        if (driveDTO.getDriverId() != null) {
            System.out.println("Fetching driver with ID: " + driveDTO.getDriverId());
            driver = driverRepository.findById(driveDTO.getDriverId())
                    .orElseThrow(() -> {
                        System.out.println("Invalid Driver ID: " + driveDTO.getDriverId());
                        return new IllegalArgumentException("Invalid Driver ID: " + driveDTO.getDriverId());
                    });
            System.out.println("Fetched driver: " + driver);
        }

        Drive newDrive = EntityToDTOMapper.mapToDrive(driveDTO, driver);
        System.out.println("Mapped new drive: " + newDrive);

        // Check if the existing Drive needs to be updated
        if (updateDriveIfNecessary(existingDrive, newDrive)) {
            System.out.println("Drive requires update. Saving updated drive.");
            Drive savedDrive = driveRepository.save(existingDrive);
            System.out.println("Updated drive saved: " + savedDrive);

            // Check if the drive is now complete and should be added to the calendar
            if (isDriveComplete(savedDrive)) {
                System.out.println("Drive is complete. Adding to calendar.");
                driveScheduleService.addDriveToCalendar(savedDrive.getId());
            } else {
                System.out.println("Drive is not complete. Not adding to calendar.");
            }

            return savedDrive;
        } else {
            System.out.println("No updates required for drive with ID: " + driveId);
        }

        return existingDrive;
    }




    @Override
    public void deleteDrive(Long driveId) {
        Drive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new NoSuchElementException("Drive not found for ID: " + driveId));

        TeeTime teeTime = drive.getTeeTime();
        if (teeTime != null) {
            teeTime.setDrive(null);
            teeTimeRepository.save(teeTime);
        }

        driveRepository.delete(drive);
    }

//    @Override
//    public List<Drive> getUntrackedDrives() {
//        LocalDate currentDate = LocalDate.now();
//
//        List<Drive> untrackedDrives = new ArrayList<>();
//
//        // Process TeeTimes
//        for (TeeTime teeTime : teeTimeRepository.findAll()) {
//            if (!teeTime.isNeedTransport()) {
//                continue;
//            }
//
//            List<Drive> newDrives = teeTimeDriveFactory.createDrivesForTeeTime(teeTime);
//
//            for (Drive newDrive : newDrives) {
//                // Refresh drives without calendar each time
//                Optional<Drive> existingDriveOpt = driveRepository
//                        .findDrivesWithoutCalendar()
//                        .stream()
//                        .filter(d -> d.getDate().equals(newDrive.getDate())
//                                && d.getCustomReason().equals(newDrive.getCustomReason())
//                                && d.getTeeTime() == teeTime)
//                        .findFirst();
//
//                if (existingDriveOpt.isPresent()) {
//                    untrackedDrives.add(existingDriveOpt.get());
//                } else if (newDrive.getDate().isAfter(currentDate)) {
//                    Drive savedDrive = driveRepository.save(newDrive);
//                    untrackedDrives.add(savedDrive);
//                }
//            }
//        }
//
//        // Process TransportationReservations
//        for (TransportationReservation reservation : transportationReservationRepository.findAll()) {
//            List<Drive> newDrives = TransportationDriveFactory.createDrivesForTransportation(reservation);
//
//            for (Drive newDrive : newDrives) {
//                Optional<Drive> existingDriveOpt = driveRepository
//                        .findDrivesWithoutCalendar()
//                        .stream()
//                        .filter(d -> d.getDate().equals(newDrive.getDate())
//                                && d.getCustomReason().equals(newDrive.getCustomReason())
//                                && d.getTransportationReservation() == reservation)
//                        .findFirst();
//
//                if (existingDriveOpt.isPresent()) {
//                    untrackedDrives.add(existingDriveOpt.get());
//                } else if (newDrive.getDate().isAfter(currentDate)) {
//                    Drive savedDrive = driveRepository.save(newDrive);
//                    untrackedDrives.add(savedDrive);
//                }
//            }
//        }
//
//        return untrackedDrives;
//    }


    @Override
    public List<Drive> getUntrackedDrives() {
        LocalDate currentDate = LocalDate.now();
        List<Drive> untrackedDrives = new ArrayList<>();

        // Fetch all existing untracked drives from the database
        List<Drive> existingUntrackedDrives = driveRepository.findDrivesWithoutCalendar();

        System.out.println("DEBUG: Found " + existingUntrackedDrives.size() + " untracked drives in the DB.");

        // ✅ Add all existing untracked drives FIRST
        untrackedDrives.addAll(existingUntrackedDrives);

        // ✅ Process TeeTime-based drives
        for (TeeTime teeTime : teeTimeRepository.findAll()) {
            if (!teeTime.isNeedTransport()) continue;

            List<Drive> newDrives = teeTimeDriveFactory.createDrivesForTeeTime(teeTime);

            for (Drive newDrive : newDrives) {
                if (newDrive.getDate().isBefore(currentDate)) continue; // Skip past drives

                Optional<Drive> existingDriveOpt = existingUntrackedDrives.stream()
                        .filter(d -> d.getDate().equals(newDrive.getDate()) &&
                                d.getCustomReason().equals(newDrive.getCustomReason()) &&
                                d.getTeeTime() == teeTime)
                        .findFirst();

                if (existingDriveOpt.isEmpty()) {
                    Drive savedDrive = driveRepository.save(newDrive);
                    untrackedDrives.add(savedDrive);
                }
            }
        }

        // ✅ Process TransportationReservation-based drives
        for (TransportationReservation reservation : transportationReservationRepository.findAll()) {
            List<Drive> newDrives = TransportationDriveFactory.createDrivesForTransportation(reservation);

            for (Drive newDrive : newDrives) {
                if (newDrive.getDate().isBefore(currentDate)) continue; // Skip past drives

                Optional<Drive> existingDriveOpt = existingUntrackedDrives.stream()
                        .filter(d -> d.getDate().equals(newDrive.getDate()) &&
                                d.getCustomReason().equals(newDrive.getCustomReason()) &&
                                d.getTransportationReservation() == reservation)
                        .findFirst();

                if (existingDriveOpt.isEmpty()) {
                    Drive savedDrive = driveRepository.save(newDrive);
                    untrackedDrives.add(savedDrive);
                }
            }
        }

        System.out.println("DEBUG: Returning " + untrackedDrives.size() + " total untracked drives.");
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
        if (!areEqual(existingDrive.getPriceIds(), newDrive.getPriceIds())) {
            existingDrive.setPriceIds(newDrive.getPriceIds());
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

    private boolean isDriveComplete(Drive drive) {
        return drive.getUserIds() != null && !drive.getUserIds().isEmpty()
                && drive.getDeparturePlace() != null && !drive.getDeparturePlace().isBlank()
                && drive.getArrivalPlace() != null && !drive.getArrivalPlace().isBlank()
                && drive.getPickupTime() != null
                && drive.getDropoffTime() != null
                && drive.getDate() != null
                && drive.getDriver() != null;
    }



}

