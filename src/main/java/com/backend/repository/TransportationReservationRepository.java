package com.backend.repository;

import com.backend.entity.TransportationReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportationReservationRepository extends JpaRepository<TransportationReservation, Integer> {
    // Custom method to find reservations by orderId
   // List<TransportationReservation> findByOrderId(Integer orderId);
}