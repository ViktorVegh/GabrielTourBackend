package com.backend.repository;

import com.backend.entity.Transportation.DrivesCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrivesCalendarRepository extends JpaRepository<DrivesCalendar, Long> {
    Optional<DrivesCalendar> findById(Long id);

}
