package com.backend.repository;

import com.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DriveRepository extends JpaRepository<Drive, Long> {
    Optional<Drive> findByDateAndCustomReasonAndTeeTime(LocalDate date, String customReason, TeeTime teeTime);
    Optional<Drive> findByDateAndCustomReasonAndTransportationReservation(LocalDate date, String customReason, TransportationReservation reservation);
}
