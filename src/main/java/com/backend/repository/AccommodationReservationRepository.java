package com.backend.repository;

import com.backend.entity.Acommodation.AccommodationReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationReservationRepository extends JpaRepository<AccommodationReservation, Integer> {
    List<AccommodationReservation> findByOrderDetail_Id(Integer orderId);
}
