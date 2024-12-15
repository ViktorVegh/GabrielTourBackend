package com.backend.service;

import com.backend.dtos.TeeTimeDTO;
import com.backend.dtos.TeeTimeRequest;
import com.backend.dtos.EntityToDTOMapper;
import com.backend.entity.Drive;
import com.backend.entity.GolfCourse;
import com.backend.entity.TeeTime;
import com.backend.entity.User;
import com.backend.repository.DriveRepository;
import com.backend.repository.GolfCourseRepository;
import com.backend.repository.TeeTimeRepository;
import com.backend.repository.UserRepository;
import com.backend.service_interface.TeeTimeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeeTimeService implements TeeTimeServiceInterface {

    @Autowired
    private TeeTimeRepository teeTimeRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private GolfCourseRepository golfCourseRepository;

    @Override
    public List<TeeTimeDTO> getTeeTimesByUserId(Long userId) {
        User user = userRepository.findByProfisId(userId.intValue())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(userId);
        return teeTimeRepository.findByProfisId(userId) // Assuming `findByUsersContaining` is available in `TeeTimeRepository`
                .stream()
                .map(EntityToDTOMapper::toTeeTimeDTO) // Updated method name
                .collect(Collectors.toList());
    }

    @Override
    public List<TeeTimeDTO> getLatestTeeTimes() {
        Pageable topFive = PageRequest.of(0, 5);
        return teeTimeRepository.findLatestTeeTimes(topFive) // Assuming `findByUsersContaining` is available in `TeeTimeRepository`
                .stream()
                .map(EntityToDTOMapper::toTeeTimeDTO) // Updated method name
                .collect(Collectors.toList());
    }

    @Override
    public TeeTimeDTO createTeeTime(TeeTimeRequest teeTimeRequest) {
        System.out.println(teeTimeRequest.getUserIds());

        // Ensure the IDs are converted to Integer for the repository
        List<User> users = teeTimeRequest.getUserIds().stream()
                .map(profisId -> userRepository.findByProfisId(profisId.intValue()) // Convert Long to Integer
                        .orElseThrow(() -> new RuntimeException("No user found with Profis ID: " + profisId)))
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            throw new RuntimeException("No valid users found for provided Profis IDs");
        }

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
        newTeeTime.setNeedTransport(teeTimeRequest.isNeedTransport());

        // Save and map to DTO
        TeeTime savedTeeTime = teeTimeRepository.save(newTeeTime);
        return EntityToDTOMapper.toTeeTimeDTO(savedTeeTime);
    }

    @Override
    public TeeTime editTeeTime(TeeTimeDTO teeTimeDTO) {
        TeeTime existingTeeTime = teeTimeRepository.getReferenceById(teeTimeDTO.getId());

        // Check if the existing TeeTime needs to be updated
        if (updateTeeTimeIfNecessary(existingTeeTime, teeTimeDTO)) {
            return teeTimeRepository.save(existingTeeTime); // Save only if updated
        }

        // If no updates were necessary, return the existing TeeTime
        return existingTeeTime;
    }

    @Override
    public void deleteTeeTime(Long id) {
        TeeTime teeTime = teeTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TeeTime not found for ID: " + id));

        if (teeTime.getDrive() != null) {
            driveRepository.delete(teeTime.getDrive());
        }

        teeTimeRepository.deleteById(id);
    }

    private boolean updateTeeTimeIfNecessary(TeeTime existingTeeTime, TeeTimeDTO newTeeTime) {
        boolean isUpdated = false;

        if (!areEqual(existingTeeTime.getTeeTime(), newTeeTime.getTeeTime())) {
            existingTeeTime.setTeeTime(newTeeTime.getTeeTime());
            isUpdated = true;
        }
        if (!areEqual(existingTeeTime.isNeedTransport(), newTeeTime.isNeedTransport())) {
            existingTeeTime.setNeedTransport(newTeeTime.isNeedTransport());
            isUpdated = true;
        }
        if (!areEqual(existingTeeTime.getAdults(), newTeeTime.getAdults())) {
            existingTeeTime.setAdults(newTeeTime.getAdults());
            isUpdated = true;
        }
        if (!areEqual(existingTeeTime.isGreen(), newTeeTime.isGreen())) {
            existingTeeTime.setGreen(newTeeTime.isGreen());
            isUpdated = true;
        }
        if (!areEqual(existingTeeTime.getHoles(), newTeeTime.getHoles())) {
            existingTeeTime.setHoles(newTeeTime.getHoles());
            isUpdated = true;
        }

        return isUpdated;
    }

    private boolean areEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }
}
