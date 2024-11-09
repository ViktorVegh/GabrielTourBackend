package com.backend.profis_service;

import com.backend.dtos.PersonDTO;
import com.backend.entity.Person;
import com.backend.entity.User;
import com.backend.entity.Driver;
import com.backend.entity.TourGuide;
import com.backend.repository.UserRepository;
import com.backend.repository.DriverRepository;
import com.backend.repository.TourGuideRepository;
import com.backend.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.dtos.EntityToDTOMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TourGuideRepository tourGuideRepository;

    @Autowired
    private OfficeRepository officeRepository;

    public List<PersonDTO> getAllPersonsFromAllRepositories() {
        List<PersonDTO> allPersons = new ArrayList<>();

        allPersons.addAll(userRepository.findAll().stream()
                .map(EntityToDTOMapper::mapToPersonDTO)
                .toList()); // Replaced with toList()
        allPersons.addAll(driverRepository.findAll().stream()
                .map(EntityToDTOMapper::mapToPersonDTO)
                .toList()); // Replaced with toList()
        allPersons.addAll(tourGuideRepository.findAll().stream()
                .map(EntityToDTOMapper::mapToPersonDTO)
                .toList()); // Replaced with toList()

        return allPersons;
    }


    public Optional<PersonDTO> findPersonByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return user.map(EntityToDTOMapper::mapToPersonDTO);
        }

        Optional<Driver> driver = Optional.ofNullable(driverRepository.findByEmail(email));
        if (driver.isPresent()) {
            return driver.map(EntityToDTOMapper::mapToPersonDTO);
        }

        Optional<TourGuide> tourGuide = Optional.ofNullable(tourGuideRepository.findByEmail(email));
        if (tourGuide.isPresent()) {
            return tourGuide.map(EntityToDTOMapper::mapToPersonDTO);
        }

        return Optional.empty();
    }

    public Optional<PersonDTO> findPersonById(Long id, String role) {
        switch (role.toUpperCase()) {
            case "USER":
                Optional<User> user = userRepository.findById(id);
                return user.map(EntityToDTOMapper::mapToPersonDTO);

            case "DRIVER":
                Optional<Driver> driver = driverRepository.findById(id);
                return driver.map(EntityToDTOMapper::mapToPersonDTO);

            case "TOUR_GUIDE":
                Optional<TourGuide> tourGuide = tourGuideRepository.findById(id);
                return tourGuide.map(EntityToDTOMapper::mapToPersonDTO);

            default:
                return Optional.empty();
        }
    }

}
