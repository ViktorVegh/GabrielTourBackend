package com.backend.repository;

import com.backend.entity.TeeTime;
import com.backend.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TeeTimeRepository extends JpaRepository<TeeTime, Long> {
    List<TeeTime> findByUsersContaining(User user);

    @Query("SELECT t FROM TeeTime t JOIN t.users u WHERE u.profis_id = :profisId")
    List<TeeTime> findByProfisId(@Param("profisId") Long profisId);
    @Query("SELECT t FROM TeeTime t ORDER BY t.teeTime DESC")
    List<TeeTime> findLatestTeeTimes(Pageable pageable);

    List<TeeTime> findAllByTeeTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
