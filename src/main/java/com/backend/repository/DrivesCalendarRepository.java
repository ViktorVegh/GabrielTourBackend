package com.backend.repository;

import com.backend.entity.Drive;
import com.backend.entity.DrivesCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DrivesCalendarRepository extends JpaRepository<DrivesCalendar, Long> {
    Optional<DrivesCalendar> findById(Long id);

}
