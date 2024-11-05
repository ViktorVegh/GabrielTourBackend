package com.backend.dtos;

public class GolfCourseDTO {
    private Long id;
    private String name;
    private ResortDTO resort; // Optional

    // Constructor
    public GolfCourseDTO() {
    }

    public GolfCourseDTO(Long id, String name, ResortDTO resort) {
        this.id = id;
        this.name = name;
        this.resort = resort;
    }

    // Getters and setters
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

    public ResortDTO getResort() {
        return resort;
    }

    public void setResort(ResortDTO resort) {
        this.resort = resort;
    }
}
