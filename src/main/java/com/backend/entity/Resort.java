package com.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Resort {
    private Long id;
    private String name;
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

    // Getter and Setter for country
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Getter and Setter for golfCourses with OneToMany relationship
    @OneToMany(mappedBy = "resort")
    public List<GolfCourse> getGolfCourses() {
        return golfCourses;
    }

    public void setGolfCourses(List<GolfCourse> golfCourses) {
        this.golfCourses = golfCourses;
    }
}
