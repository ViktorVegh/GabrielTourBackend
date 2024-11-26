package com.backend.repository;

import com.backend.entity.Hotel;
import com.backend.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
}
