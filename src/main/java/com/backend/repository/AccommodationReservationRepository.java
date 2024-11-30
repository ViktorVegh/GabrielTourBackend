package com.backend.repository;

import com.backend.entity.AccommodationReservation;
import com.backend.entity.OrderDetail;
import com.backend.entity.TransportationReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationReservationRepository extends JpaRepository<AccommodationReservation, Integer> {
    List<AccommodationReservation> findByOrderDetail_Id(Integer orderId);
}
