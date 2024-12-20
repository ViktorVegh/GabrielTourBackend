package com.backend.auth;

import com.backend.entity.Person.*;
import com.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService implements AuthServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TourGuideRepository tourGuideRepository;
    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private DriverManagerRepository driverManagerRepository;

    // Registration
    @Override
    public String registerEmployee(String email, String password, String name, String profilePicture, String role) {
        // Check if email is already registered
        if (userRepository.findByEmail(email) != null || driverRepository.findByEmail(email) != null || tourGuideRepository.findByEmail(email) != null  || driverManagerRepository.findByEmail(email) != null){
            throw new RuntimeException("Email is already registered!");
        }

        // Encrypt the password before saving
        String encryptedPassword = EncryptionUtil.encrypt(password);

        // Handle optional fields
        String finalName = (name != null) ? name : "";
        String finalProfilePicture = (profilePicture != null) ? profilePicture : "";

        Person newPerson;

        // Save the appropriate user type
        switch (role.toLowerCase()) {
            case "driver":
                newPerson = new Driver(email, encryptedPassword, finalName, finalProfilePicture, role);
                driverRepository.save((Driver) newPerson);
                break;
            case "tourguide":
                newPerson = new Delegate(email, encryptedPassword, finalName, finalProfilePicture, role);
                tourGuideRepository.save((Delegate) newPerson);
                break;
            case "office":
                newPerson = new Office(email, encryptedPassword, role);
                officeRepository.save((Office) newPerson);
                break;
            case "drivermanager":
                newPerson = new DriverManager(email, encryptedPassword, finalName, finalProfilePicture, role);
                driverManagerRepository.save((DriverManager) newPerson);
                break;


            default:
                newPerson = new User(email, encryptedPassword, finalName, finalProfilePicture, role);
                userRepository.save((User) newPerson);
                break;
        }

        // Generate a token with the role
        return jwtUtil.generateToken(newPerson.getId(), role);
    }
    @Override
    public void registerUser(String email, String password, Long clientId){
        try {
            Person newPerson;
            String role = "user";
            String encryptedPassword = EncryptionUtil.encrypt(password);
            newPerson = new User(email, encryptedPassword, clientId, role);
            userRepository.save((User) newPerson);
            }
            catch (Exception e){e.printStackTrace();}

    }
    // Login
    @Override
    public String login(String email, String password) {
        // Try finding the user in each repositoryoffice
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
        if (person == null) {
            person = driverManagerRepository.findByEmail(email);
        }
        // Check if the person was found and the password matches
        if (person != null && Objects.equals(password, EncryptionUtil.decrypt(person.getPassword())))
        {
            String role;

            // Corrected role assignment with proper braces
            if (person instanceof Driver) {
                role = "driver";
            } else if (person instanceof Delegate) {
                role = "tourguide";
            } else if (person instanceof Office) {
                role = "office";
            } else if (person instanceof DriverManager) {
                role = "drivermanager";
            } else {
                role = "user";
            }

            return jwtUtil.generateToken(person.getId(), role);  // Pass the role when generating the token
        }

        throw new RuntimeException("Invalid login credentials!");
    }

    @Override
    public void logout() {

    }
}
