package com.backend.profis_service;

import com.backend.dtos.TeeTimeDTO;
import com.backend.dtos.TeeTimeRequest;
import com.backend.dtos.EntityToDTOMapper;
import com.backend.entity.GolfCourse;
import com.backend.entity.TeeTime;
import com.backend.entity.User;
import com.backend.repository.GolfCourseRepository;
import com.backend.repository.TeeTimeRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeeTimeService {

    @Autowired
    private TeeTimeRepository teeTimeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GolfCourseRepository golfCourseRepository;

    public List<TeeTimeDTO> getTeeTimesByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(teeTimeRepository::findByUser)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .stream()
                .map(EntityToDTOMapper::mapToTeeTimeDTO)
                .collect(Collectors.toList());
    }

    public TeeTimeDTO createTeeTime(TeeTimeRequest teeTimeRequest) {
        User user = userRepository.findById(teeTimeRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User with ID " + teeTimeRequest.getUserId() + " not found"));

        GolfCourse fetchedGolfCourse = golfCourseRepository.findById(teeTimeRequest.getGolfCourseId())
                .orElseThrow(() -> new RuntimeException("GolfCourse with ID " + teeTimeRequest.getGolfCourseId() + " not found"));

        TeeTime newTeeTime = new TeeTime();
        newTeeTime.setUser(user);
        newTeeTime.setGolfCourse(fetchedGolfCourse);
        newTeeTime.setTeeTime(teeTimeRequest.getTeeTime());
        newTeeTime.setGroupSize(teeTimeRequest.getGroupSize());

        TeeTime savedTeeTime = teeTimeRepository.save(newTeeTime);
        return EntityToDTOMapper.mapToTeeTimeDTO(savedTeeTime);
    }
}
