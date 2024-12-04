package com.backend.repository;

import com.backend.entity.TeeTime;
import com.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TeeTimeRepository extends JpaRepository<TeeTime, Long> {
    List<TeeTime> findByUsersContaining(User user);
    List<TeeTime> findAllByTeeTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
