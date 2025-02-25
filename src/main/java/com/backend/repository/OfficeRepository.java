package com.backend.repository;


import com.backend.entity.Person.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    Office findByEmail(String email);
}
