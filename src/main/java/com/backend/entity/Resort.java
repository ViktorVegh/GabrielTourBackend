package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Resort {
    private Long id;
    private String name;
    private String address;
    private String country;
    private List<GolfCourse> golfCourses;

    // Getter and Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    // Getter and Setter for golfCourses with OneToMany relationship
    @JsonManagedReference
    @OneToMany(mappedBy = "resort")
    public List<GolfCourse> getGolfCourses() {
        return golfCourses;
    }

    public Resort(Long id, String name, String address, String country, List<GolfCourse> golfCourses) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.country = country;
        this.golfCourses = golfCourses;
    }

    public Resort() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setGolfCourses(List<GolfCourse> golfCourses) {
        this.golfCourses = golfCourses;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
