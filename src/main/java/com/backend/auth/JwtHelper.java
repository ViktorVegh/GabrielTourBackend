package com.backend.auth;

import com.backend.entity.Person.Person;
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
    public JwtHelper(UserRepository userRepository, DriverRepository driverRepository, TourGuideRepository tourGuideRepository, OfficeRepository officeRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.tourGuideRepository = tourGuideRepository;
        this.officeRepository = officeRepository;
    }

    // Validate the token and retrieve the Person entity based on userId
    public Person validateTokenAndRetrievePerson(String token) {
        Long userId = JwtUtil.validateToken(token);

        if (userId == null) {
            return null;  // Invalid or expired token
        }

        // Find the Person by ID from the relevant repositories
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

        return person;  // Returns null if no matching Person is found
    }

    // Extract the role from the token's claims
    public String getRoleFromToken(String token) {
        Claims claims = JwtUtil.getClaimsFromToken(token);
        return claims != null ? claims.get("role", String.class) : null;
    }

    public Integer getIdFromToken(String token) {
        Claims claims;
        try {
            System.out.println("i got to token");
            claims = JwtUtil.getClaimsFromToken(token);
        } catch (Exception e) {
            // Log exception (e.g., expired or malformed token)
            return null;
        }
        if (claims == null) return null;
        try {
            return Integer.parseInt(claims.get("id", String.class));
        } catch (NumberFormatException e) {
            // Log error: Invalid format for 'id' in token claims
            return null;
        }
    }
}
