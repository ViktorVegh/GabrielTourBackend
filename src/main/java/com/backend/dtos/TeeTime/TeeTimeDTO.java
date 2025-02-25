package com.backend.dtos.TeeTime;

import java.time.LocalDateTime;
import java.util.List;

public class TeeTimeDTO {
    private Long id;
    private LocalDateTime teeTime;
    private int groupSize;
    private List<Long> userIds; // List of user IDs
    private Long golfCourseId; // Changed to Long for golfCourseId
    private boolean green;
    private int holes;
    private int adults;
    private int juniors;
    private String note;
    private boolean needTransport;

    // Default constructor
    public TeeTimeDTO() {
    }

    // Full constructor
    public TeeTimeDTO(Long id, LocalDateTime teeTime, int groupSize, List<Long> userIds, Long golfCourseId,
                      boolean green, int holes, int adults, int juniors, String note, boolean needTransport) {
        this.id = id;
        this.teeTime = teeTime;
        this.groupSize = groupSize;
        this.userIds = userIds;
        this.golfCourseId = golfCourseId;
        this.green = green;
        this.holes = holes;
        this.adults = adults;
        this.juniors = juniors;
        this.note = note;
        this.needTransport = needTransport;
    }

    // Getters and setters for all fields
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

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getGolfCourseId() {
        return golfCourseId; // Getter for golfCourseId
    }

    public void setGolfCourseId(Long golfCourseId) {
        this.golfCourseId = golfCourseId; // Setter for golfCourseId
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

    public boolean isNeedTransport() {
        return needTransport;
    }

    public void setNeedTransport(boolean needTransport) {
        this.needTransport = needTransport;
    }
}
