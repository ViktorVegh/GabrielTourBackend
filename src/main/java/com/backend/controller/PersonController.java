package com.backend.controller;

import com.backend.dtos.PersonDTO;
import com.backend.profis_service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PreAuthorize("hasRole('office')")
    @GetMapping("/all")
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        List<PersonDTO> persons = personService.getAllPersonsFromAllRepositories();
        return ResponseEntity.ok(persons);
    }

    @PreAuthorize("hasRole('office')")
    @GetMapping("/search_by_email")
    public ResponseEntity<PersonDTO> searchPersonByEmail(@RequestParam String email) {
        return personService.findPersonByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('office')")
    @GetMapping(value = "/{id}/{role}", produces = "application/json")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id, @PathVariable String role) {
        return personService.findPersonById(id, role)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



}
