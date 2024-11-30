package com.backend.repository;

import com.backend.entity.Prices;
import com.backend.entity.TransportationReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Prices, Integer> {
    List<Prices> findByOrderDetail_Id(Integer orderId);
}
