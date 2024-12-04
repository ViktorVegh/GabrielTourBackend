package com.backend.repository;

import com.backend.entity.AccommodationReservation;
import com.backend.entity.Drive;
import com.backend.entity.OrderDetail;
import com.backend.entity.TransportationReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DriveRepository extends JpaRepository<Drive, Long> {
    List<Drive> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
