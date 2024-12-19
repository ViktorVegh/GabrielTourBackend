package com.backend.repository;

import com.backend.entity.Person.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Query("SELECT u.password FROM User u WHERE u.id = :id")
    String getPasswordById(@Param("id") int id);

    @Query("SELECT u.profis_id FROM User u WHERE u.id = :id")
    Integer getProfisId(@Param("id") int id);

    @Query("SELECT u FROM User u WHERE u.profis_id = :profisId")
    Optional<User> findByProfisId(@Param("profisId") Integer profisId);
}
