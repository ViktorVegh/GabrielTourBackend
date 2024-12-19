package com.backend.repository;

import com.backend.entity.Acommodation.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    @Query("SELECT h.name FROM Hotel h " +
            "JOIN AccommodationReservation ar ON h.id = ar.hotel.id " +
            "JOIN ar.orderDetail od " +
            "JOIN OrderUser ou ON ou.orderDetail.id = od.id " +
            "WHERE ou.user.id = :userId")
    Optional<String> findFirstHotelNameByUserId(@Param("userId") Integer userId);
}

