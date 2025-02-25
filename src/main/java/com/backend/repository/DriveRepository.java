package com.backend.repository;

import com.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DriveRepository extends JpaRepository<Drive, Long> {
    Optional<Drive> findByDateAndCustomReasonAndTeeTime(LocalDate date, String customReason, TeeTime teeTime);
    Optional<Drive> findByDateAndCustomReasonAndTransportationReservation(LocalDate date, String customReason, TransportationReservation reservation);
    @Query(value = "SELECT * FROM drive d WHERE d.calendar_id IS NULL AND d.date >= CURRENT_DATE", nativeQuery = true)
    List<Drive> findDrivesWithoutCalendar();



}
