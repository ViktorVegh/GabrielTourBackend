package com.backend.repository;

import com.backend.entity.GolfCourse;
import com.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GolfCourseRepository extends JpaRepository<GolfCourse, Long> {
    // Additional custom queries can be added here if needed
    @Query("SELECT g FROM GolfCourse g WHERE LOWER(g.name) = LOWER(:name)")
    GolfCourse findByName(@Param("name") String name);
}
