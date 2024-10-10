package com.backend.auth;

import com.backend.entity.Person;
import com.backend.repository.UserRepository;
import com.backend.repository.DriverRepository;
import com.backend.repository.TourGuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtHelper {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final TourGuideRepository tourGuideRepository;

    @Autowired
    public JwtHelper(UserRepository userRepository, DriverRepository driverRepository, TourGuideRepository tourGuideRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.tourGuideRepository = tourGuideRepository;
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

        return person;
    }
}
