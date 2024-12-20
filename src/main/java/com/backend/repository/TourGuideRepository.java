package com.backend.repository;

import com.backend.entity.Person.Delegate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourGuideRepository extends JpaRepository<Delegate, Long> {
    Delegate findByEmail(String email);
}
