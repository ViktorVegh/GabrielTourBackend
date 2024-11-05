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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resort_id", nullable = true)
    private Resort resort;

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

    public Resort getResort() {
        return resort;
    }

    public void setResort(Resort resort) {
        this.resort = resort;
    }

    public List<TeeTime> getTeeTimes() {
        return teeTimes;
    }

    public void setTeeTimes(List<TeeTime> teeTimes) {
        this.teeTimes = teeTimes;
    }
}
