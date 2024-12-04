package com.backend.service_interface;

import com.backend.dtos.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface PersonServiceInterface {
    List<PersonDTO> getAllPersonsFromAllRepositories();

    Optional<PersonDTO> findPersonByEmail(String email);

    Optional<PersonDTO> findPersonById(Long id, String role);
}
