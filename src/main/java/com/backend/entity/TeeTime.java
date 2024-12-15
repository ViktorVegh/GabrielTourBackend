package com.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TeeTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Associated user who booked the tee time
    @ManyToMany
    @JoinTable(
            name = "tee_time_users",
            joinColumns = @JoinColumn(name = "tee_time_id"),
            inverseJoinColumns = @JoinColumn(name = "profis_id", referencedColumnName = "profis_id")
    )
    private List<User> users;

    // Date and time of the tee time
    @Column(nullable = false)
    private LocalDateTime teeTime;

    // Indicates whether the tee time is on the green
    private boolean green;

    // Number of holes (9 or 18)
    private int holes;

    // Associated golf course for the tee time
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "golf_course_id", nullable = false)
    private GolfCourse golfCourse;

    // Group size for the tee time
    @Column(nullable = false)
    private Integer groupSize;

    // Number of adults in the group
    private int adults;

    // Number of juniors in the group
    private int juniors;

    // Additional notes for the tee time reservation
    private String note;

    // Drive details (pickup, drop-off, driver, etc.)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "drive_id", referencedColumnName = "id")
    private Drive drive;

    @Column(nullable = false)
    private boolean needTransport;

    // Getters and Setters

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public GolfCourse getGolfCourse() {
        return golfCourse;
    }

    public void setGolfCourse(GolfCourse golfCourse) {
        this.golfCourse = golfCourse;
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

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    public boolean isNeedTransport() {
        return needTransport;
    }

    public void setNeedTransport(boolean needTransport) {
        this.needTransport = needTransport;
    }
}
