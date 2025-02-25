package com.backend.service;


import com.backend.dtos.EntityToDTOMapper;
import com.backend.dtos.TeeTime.GolfCourseDTO;
import com.backend.entity.TeeTime.GolfCourse;
import com.backend.repository.GolfCourseRepository;
import com.backend.service_interface.GolfCourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GolfCourseService implements GolfCourseServiceInterface {
    @Autowired
    GolfCourseRepository golfCourseRepository;

    @Override
    public Optional<GolfCourseDTO> findGolfCourse(String name) {
        Optional<GolfCourse> golfCourse = Optional.ofNullable(golfCourseRepository.findByName(name));
        if (golfCourse.isPresent()) {
            return golfCourse.map(EntityToDTOMapper::mapToGolfCourseDTO);
        }
        return Optional.empty();
    }

    @Override
    public GolfCourseDTO createGolfCourse(GolfCourseDTO golfCourseDTO) {
        Optional<GolfCourse> existingGolfCourse = Optional.ofNullable(golfCourseRepository.findByName(golfCourseDTO.getName()));
        if (existingGolfCourse.isPresent()){
            throw new IllegalArgumentException("Golf course with the name '" + golfCourseDTO.getName() + "' already exists.");
        }
        else {
            GolfCourse golfCourse = new GolfCourse();
            golfCourse.setName(golfCourseDTO.getName());
            GolfCourse savedGolfCourse =golfCourseRepository.save(golfCourse);
            return EntityToDTOMapper.toGolfCourseDTO(savedGolfCourse);
        }

    }
}
