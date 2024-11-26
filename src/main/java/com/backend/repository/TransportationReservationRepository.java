package com.backend.repository;

import com.backend.entity.TransportationReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationReservationRepository extends JpaRepository<TransportationReservation, Integer> {
}
