package com.backend.dtos;

import java.time.LocalDateTime;

public class TeeTimeDTO {
    private Long id;
    private LocalDateTime teeTime;
    private int groupSize;
    private Long userId; // Add userId
    private GolfCourseDTO golfCourse;

    // Constructor
    public TeeTimeDTO() {
    }

    public TeeTimeDTO(Long id, LocalDateTime teeTime, int groupSize, Long userId, GolfCourseDTO golfCourse) {
        this.id = id;
        this.teeTime = teeTime;
        this.groupSize = groupSize;
        this.userId = userId;
        this.golfCourse = golfCourse;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTeeTime() {
        return teeTime;
    }

    public void setTeeTime(LocalDateTime teeTime) {
        this.teeTime = teeTime;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public GolfCourseDTO getGolfCourse() {
        return golfCourse;
    }

    public void setGolfCourse(GolfCourseDTO golfCourse) {
        this.golfCourse = golfCourse;
    }
}

