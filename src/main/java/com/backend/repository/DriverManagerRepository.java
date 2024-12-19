package com.backend.repository;

import com.backend.entity.Person.DriverManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverManagerRepository extends JpaRepository<DriverManager, Long> {
    DriverManager findByEmail(String email);
}

