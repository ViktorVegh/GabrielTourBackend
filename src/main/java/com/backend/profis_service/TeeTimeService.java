package com.backend.profis_service;

import com.backend.entity.Driver;
import com.backend.entity.GolfCourse;
import com.backend.entity.TeeTime;
import com.backend.entity.User;
import com.backend.repository.DriverRepository;
import com.backend.repository.GolfCourseRepository;
import com.backend.repository.TeeTimeRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TeeTimeService {

    @Autowired
    private TeeTimeRepository teeTimeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GolfCourseRepository golfCourseRepository;

    @Autowired
    private DriverRepository driverRepository;


    // Service method to get all tee times for a specific user by user ID
    public List<TeeTime> getTeeTimesByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        // Return tee times if user is found, else return empty list or handle as appropriate
        return user.map(teeTimeRepository::findByUser).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public TeeTime createTeeTime(Long userId, GolfCourse golfCourse, LocalDateTime teeTime, Integer groupSize) {
        System.out.println("i got to servicw");
        // Fetch User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        // Fetch GolfCourse
        GolfCourse FetchedGolfCourse = golfCourseRepository.findById(golfCourse.getId())
                .orElseThrow(() -> new RuntimeException("GolfCourse with ID " + golfCourse.getId() + " not found"));


        // Create and populate TeeTime
        TeeTime newTeeTime = new TeeTime();
        newTeeTime.setUser(user);
        newTeeTime.setGolfCourse(FetchedGolfCourse);
        newTeeTime.setTeeTime(teeTime);
        newTeeTime.setGroupSize(groupSize);

        // Save the new TeeTime
        return teeTimeRepository.save(newTeeTime);
    }
}
