package com.backend.service;

import com.backend.dtos.TeeTimeDTO;
import com.backend.dtos.TeeTimeRequest;
import com.backend.dtos.EntityToDTOMapper;
import com.backend.entity.GolfCourse;
import com.backend.entity.TeeTime;
import com.backend.entity.User;
import com.backend.repository.GolfCourseRepository;
import com.backend.repository.TeeTimeRepository;
import com.backend.repository.UserRepository;
import com.backend.service_interface.TeeTimeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeeTimeService implements TeeTimeServiceInterface {

    @Autowired
    private TeeTimeRepository teeTimeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GolfCourseRepository golfCourseRepository;

    @Override
    public List<TeeTimeDTO> getTeeTimesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return teeTimeRepository.findByUsersContaining(user) // Assuming `findByUsersContaining` is available in `TeeTimeRepository`
                .stream()
                .map(EntityToDTOMapper::toTeeTimeDTO) // Updated method name
                .collect(Collectors.toList());
    }

    @Override
    public TeeTimeDTO createTeeTime(TeeTimeRequest teeTimeRequest) {
        System.out.println(teeTimeRequest.getUserIds());
        // Retrieve all users from the provided user IDs
        List<User> users = userRepository.findAllById(teeTimeRequest.getUserIds());
        if (users.isEmpty()) {
            throw new RuntimeException("No valid users found for provided user IDs");
        }
        System.out.println(teeTimeRequest.getGolfCourseId()+" golf c id");
        // Fetch the golf course
        GolfCourse fetchedGolfCourse = golfCourseRepository.findById(teeTimeRequest.getGolfCourseId())
                .orElseThrow(() -> new RuntimeException("GolfCourse with ID " + teeTimeRequest.getGolfCourseId() + " not found"));

        // Create a new TeeTime
        TeeTime newTeeTime = new TeeTime();
        newTeeTime.setUsers(users); // Set the list of users
        newTeeTime.setGolfCourse(fetchedGolfCourse);
        newTeeTime.setTeeTime(teeTimeRequest.getTeeTime());
        newTeeTime.setGroupSize(teeTimeRequest.getGroupSize());
        newTeeTime.setGreen(teeTimeRequest.isGreen());
        newTeeTime.setHoles(teeTimeRequest.getHoles());
        newTeeTime.setAdults(teeTimeRequest.getAdults());
        newTeeTime.setJuniors(teeTimeRequest.getJuniors());
        newTeeTime.setNote(teeTimeRequest.getNote());

        // Save and map to DTO
        TeeTime savedTeeTime = teeTimeRepository.save(newTeeTime);
        return EntityToDTOMapper.toTeeTimeDTO(savedTeeTime);
    }
}
