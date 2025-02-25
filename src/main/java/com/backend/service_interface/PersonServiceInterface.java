package com.backend.service_interface;

import com.backend.dtos.Person.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface PersonServiceInterface {
    List<PersonDTO> getAllPersonsFromAllRepositories();

    Optional<PersonDTO> findPersonByEmail(String email);

    Optional<PersonDTO> findPersonById(Long id, String role);

    List<PersonDTO> getAllDrivers();

    Optional<PersonDTO> findPersonByProfisId(Integer profisId);
}
