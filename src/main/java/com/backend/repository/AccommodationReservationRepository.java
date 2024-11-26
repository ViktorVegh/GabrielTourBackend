package com.backend.repository;

import com.backend.entity.AccommodationReservation;
import com.backend.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationReservationRepository extends JpaRepository<AccommodationReservation, Integer> {
}
