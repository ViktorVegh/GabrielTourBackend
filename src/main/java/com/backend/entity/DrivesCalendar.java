package com.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drives-calendar")
public class DrivesCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "month_start_date", nullable = false)
    private LocalDate monthStartDate;

    @Column(name = "month_end_date", nullable = false)
    private LocalDate monthEndDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "calendar_id") // Creates a foreign key in the "Drive" table
    private List<Drive> drives;

    public DrivesCalendar() {
    }

    public DrivesCalendar(LocalDate weekStartDate, LocalDate weekEndDate, List<Drive> drives) {
        this.monthStartDate = weekStartDate;
        this.monthEndDate = weekEndDate;
        this.drives = drives != null ? new ArrayList<>(drives) : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMonthStartDate() {
        return monthStartDate;
    }

    public void setMonthStartDate(LocalDate weekStartDate) {
        this.monthStartDate = weekStartDate;
    }

    public LocalDate getMonthEndDate() {
        return monthEndDate;
    }

    public void setMonthEndDate(LocalDate weekEndDate) {
        this.monthEndDate = weekEndDate;
    }


    public List<Drive> getDrives() {
        return drives;
    }

    public void setDrives(List<Drive> drives) {
        this.drives = drives != null ? new ArrayList<>(drives) : new ArrayList<>();
    }

    public void addDrive(Drive drive) {
        if (!this.drives.contains(drive)) {
            this.drives.add(drive);
        }
    }

    public void removeDrive(Drive drive) {
        this.drives.remove(drive);
    }
}
