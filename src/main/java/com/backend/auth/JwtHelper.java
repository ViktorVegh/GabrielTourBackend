package com.backend.auth;

import com.backend.entity.Person;
import com.backend.repository.OfficeRepository;
import com.backend.repository.UserRepository;
import com.backend.repository.DriverRepository;
import com.backend.repository.TourGuideRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtHelper {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final TourGuideRepository tourGuideRepository;
    private final OfficeRepository officeRepository;

    @Autowired
    public JwtHelper(UserRepository userRepository, DriverRepository driverRepository, TourGuideRepository tourGuideRepository,OfficeRepository officeRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.tourGuideRepository = tourGuideRepository;
        this.officeRepository = officeRepository;
    }

    // Validate the token and retrieve the Person entity (User, Driver, TourGuide)
    public Person validateTokenAndRetrievePerson(String token) {
        Long userId = JwtUtil.validateToken(token);

        if (userId == null) {
            return null;
        }

        // Try to find the person in each repository
        Person person = userRepository.findById(userId).orElse(null);
        if (person == null) {
            person = driverRepository.findById(userId).orElse(null);
        }
        if (person == null) {
            person = tourGuideRepository.findById(userId).orElse(null);
        }
        if (person == null) {
            person = officeRepository.findById(userId).orElse(null);
        }

        return person;
    }
    public String getRoleFromToken(String token) {
        Claims claims = JwtUtil.getClaimsFromToken(token);
        return claims != null ? claims.get("role", String.class) : null;
    }
}
