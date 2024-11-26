package com.backend.repository;

import com.backend.entity.OrderUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderUserRepository extends JpaRepository<OrderUser, Integer> {

    @Query("SELECT o.klic FROM OrderUser o WHERE o.user.id = :userId ORDER BY o.id.orderId DESC")
    String getLatestOrderKlicByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM OrderUser o WHERE o.user.id = :userId ORDER BY o.id.orderId DESC")
    Optional<OrderUser> findFirstByUserId(@Param("userId") Long userId);

}
