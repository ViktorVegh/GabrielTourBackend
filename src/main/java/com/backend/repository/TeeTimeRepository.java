package com.backend.repository;

import com.backend.entity.TeeTime;
import com.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeeTimeRepository extends JpaRepository<TeeTime, Long> {
    // Additional custom queries can be added here if needed
    List<TeeTime> findByUsersContaining(User user);
}
