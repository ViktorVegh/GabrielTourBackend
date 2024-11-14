package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class GolfCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "golfCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TeeTime> teeTimes;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


//    public List<TeeTime> getTeeTimes() {
//        return teeTimes;
//    }
//
//    public void setTeeTimes(List<TeeTime> teeTimes) {
//        this.teeTimes = teeTimes;
//    }
}
