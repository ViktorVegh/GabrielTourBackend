package com.backend.repository;

import com.backend.entity.Order.OrderUser;
import com.backend.entity.Order.OrderUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
public interface OrderUserRepository extends JpaRepository<OrderUser, OrderUserId> {
    @Query("SELECT o FROM OrderUser o WHERE o.user.id = :userId")
    List<OrderUser> findAllOrdersByUserId(@Param("userId") Long userId);

    default OrderUser findClosestOrderByUserId(Long userId) {
        List<OrderUser> orders = findAllOrdersByUserId(userId);

        if (orders.isEmpty()) {
            return null; // No orders found
        }

        // Filter out orders with null startDate and find the closest order
        // In future filter out the olders that are in past
        return orders.stream()
                .filter(o -> o.getOrderDetail() != null && o.getOrderDetail().getStartDate() != null)
                .min((o1, o2) -> {
                    long diff1 = Math.abs(o1.getOrderDetail().getStartDate().until(LocalDateTime.now(), ChronoUnit.SECONDS));
                    long diff2 = Math.abs(o2.getOrderDetail().getStartDate().until(LocalDateTime.now(), ChronoUnit.SECONDS));
                    return Long.compare(diff1, diff2);
                })
                .orElse(null);
    }
}
