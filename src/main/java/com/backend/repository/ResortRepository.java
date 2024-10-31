package com.backend.repository;

import com.backend.entity.Resort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResortRepository extends JpaRepository<Resort, Long> {
    // Additional custom queries can be added here if needed
}
