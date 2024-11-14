package com.backend.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class TeeTimeRequest {
    private List<Long> userIds; // List of user IDs for multiple users
    private Long golfCourseId;
    private LocalDateTime teeTime;
    private Integer groupSize;
    private boolean green;
    private int holes;
    private int adults;
    private int juniors;
    private String note;

        // Driver ID

    // Getters and Setters

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getGolfCourseId() {
        return golfCourseId;
    }

    public void setGolfCourseId(Long golfCourseId) {
        this.golfCourseId = golfCourseId;
    }

    public LocalDateTime getTeeTime() {
        return teeTime;
    }

    public void setTeeTime(LocalDateTime teeTime) {
        this.teeTime = teeTime;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
    }

    public boolean isGreen() {
        return green;
    }

    public void setGreen(boolean green) {
        this.green = green;
    }

    public int getHoles() {
        return holes;
    }

    public void setHoles(int holes) {
        this.holes = holes;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getJuniors() {
        return juniors;
    }

    public void setJuniors(int juniors) {
        this.juniors = juniors;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}

