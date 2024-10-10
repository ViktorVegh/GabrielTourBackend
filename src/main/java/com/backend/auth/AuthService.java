package com.backend.auth;

import com.backend.entity.Driver;
import com.backend.entity.Person;
import com.backend.entity.TourGuide;
import com.backend.entity.User;
import com.backend.repository.DriverRepository;
import com.backend.repository.TourGuideRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TourGuideRepository tourGuideRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    public String register(String email, String password, String name, String profilePicture, String role) {
        // Check all repositories to see if the email is already registered
        if (userRepository.findByEmail(email) != null || driverRepository.findByEmail(email) != null || tourGuideRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email is already registered!");
        }

        String hashedPassword = passwordEncoder.encode(password);
        String finalName = (name != null) ? name : "";  // Default to empty string if name is null
        String finalProfilePicture = (profilePicture != null) ? profilePicture : "";  // Default to empty string if profile picture is null
        Person newPerson;

        switch (role.toLowerCase()) {
            case "driver":
                newPerson = new Driver(email, hashedPassword, name, profilePicture);
                driverRepository.save((Driver) newPerson);
                break;
            case "tourguide":
                newPerson = new TourGuide(email, hashedPassword, name, profilePicture);
                tourGuideRepository.save((TourGuide) newPerson);
                break;
            default:
                newPerson = new User(email, hashedPassword, name, profilePicture);
                userRepository.save((User) newPerson);
                break;
        }

        return jwtUtil.generateToken(newPerson.getId());
    }


    public String login(String email, String password) {
        // Try finding the user in each repository
        Person person = userRepository.findByEmail(email);
        if (person == null) {
            person = driverRepository.findByEmail(email);
        }
        if (person == null) {
            person = tourGuideRepository.findByEmail(email);
        }

        // Check if the person was found and the password matches
        if (person != null && passwordEncoder.matches(password, person.getPassword())) {
            return jwtUtil.generateToken(person.getId());
        }
        throw new RuntimeException("Invalid login credentials!");
    }

    public void logout() {
    }
}
