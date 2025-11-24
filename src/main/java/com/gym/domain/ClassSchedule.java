package com.gym.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClassSchedule {
    private int scheduleId;
    private int classId;
    private LocalDate scheduledDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int availableSpots;

    public ClassSchedule(int classId, LocalDate scheduledDate, LocalTime startTime,
                         LocalTime endTime, int availableSpots) {
        this.classId = classId;
        this.scheduledDate = scheduledDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.availableSpots = availableSpots;
    }
    //From Database
    public ClassSchedule(int scheduleId, int classId, LocalDate scheduledDate, LocalTime startTime, LocalTime endTime, int availableSpots) {
        this.scheduleId = scheduleId;
        this.classId = classId;
        this.scheduledDate = scheduledDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.availableSpots = availableSpots;
    }

    // Getters
    public int getScheduleId() { return scheduleId; }
    public int getClassId() { return classId; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getAvailableSpots() { return availableSpots; }

    // Setters
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public void setAvailableSpots(int availableSpots) { this.availableSpots = availableSpots; }

    public boolean isFull() {
        return availableSpots <= 0;
    }
    public boolean hasAvailableSpots() {
        return availableSpots > 0;
    }

    public void decrementSpots() {
        if (availableSpots > 0) {
            availableSpots--;
        }
    }

    public void incrementSpots() {
        availableSpots++;
    }

    @Override
    public String toString() {
        return "ClassSchedule{" +
                "scheduleId=" + scheduleId +
                ", classId=" + classId +
                ", date=" + scheduledDate +
                ", time=" + startTime + "-" + endTime +
                ", availableSpots=" + availableSpots +
                '}';
    }
}