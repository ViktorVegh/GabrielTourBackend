package com.backend.repository;

import com.backend.entity.GolfCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GolfCourseRepository extends JpaRepository<GolfCourse, Long> {
    // Additional custom queries can be added here if needed
}
