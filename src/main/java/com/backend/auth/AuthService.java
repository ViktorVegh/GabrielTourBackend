package com.backend.auth;

import com.backend.entity.*;
import com.backend.repository.DriverRepository;
import com.backend.repository.OfficeRepository;
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
    private OfficeRepository officeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Registration
    public String register(String email, String password, String name, String profilePicture, String role) {
        // Check all repositories to see if the email is already registered
        if (userRepository.findByEmail(email) != null || driverRepository.findByEmail(email) != null || tourGuideRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email is already registered!");
        }

        String hashedPassword = passwordEncoder.encode(password);
        String finalName = (name != null) ? name : "";  // Default to empty string if name is null
        String finalProfilePicture = (profilePicture != null) ? profilePicture : "";  // Default to empty string if profile picture is null
        Person newPerson;

        // Save the appropriate user type and generate token
        switch (role.toLowerCase()) {
            case "driver":
                newPerson = new Driver(email, hashedPassword, name, profilePicture, role);
                driverRepository.save((Driver) newPerson);
                break;
            case "tourguide":
                newPerson = new TourGuide(email, hashedPassword, name, profilePicture,role);
                tourGuideRepository.save((TourGuide) newPerson);
                break;
            case "office":
                newPerson = new Office(email,hashedPassword,role);
                officeRepository.save((Office) newPerson);
                break;
            default:
                newPerson = new User(email, hashedPassword, name, profilePicture,role);
                userRepository.save((User) newPerson);
                break;
        }

        // Generate a token with the role
        return jwtUtil.generateToken(newPerson.getId(), role);  // Pass the role when generating the token
    }

    // Login
    public String login(String email, String password) {
        // Try finding the user in each repository
        Person person = userRepository.findByEmail(email);
        if (person == null) {
            person = driverRepository.findByEmail(email);
        }
        if (person == null) {
            person = tourGuideRepository.findByEmail(email);
        }
        if (person == null) {
            person = officeRepository.findByEmail(email);
        }

        // Check if the person was found and the password matches
        if (person != null && passwordEncoder.matches(password, person.getPassword())) {
            String role;

            // Corrected role assignment with proper braces
            if (person instanceof Driver) {
                role = "driver";
            } else if (person instanceof TourGuide) {
                role = "tourguide";
            } else if (person instanceof Office) {
                role = "office";
            } else {
                role = "user";
            }

            return jwtUtil.generateToken(person.getId(), role);  // Pass the role when generating the token
        }

        throw new RuntimeException("Invalid login credentials!");
    }

    public void logout() {

    }
}
