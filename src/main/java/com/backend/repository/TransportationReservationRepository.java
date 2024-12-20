package com.backend.repository;

import com.backend.entity.Transportation.TransportationReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransportationReservationRepository extends JpaRepository<TransportationReservation, Integer> {

    List<TransportationReservation> findByOrderDetail_Id(Integer orderId);

    List<TransportationReservation> findAllByPickupTimeBetweenOrDropoffTimeBetween(
            LocalDateTime pickupStart,
            LocalDateTime pickupEnd,
            LocalDateTime dropoffStart,
            LocalDateTime dropoffEnd
    );

}