package com.gym.domain;

public class GymClass {
    private int classId;
    private String className;
    private String instructorName;
    private String description;
    private int trainerId;
    private int capacity;
    private int durationMinutes;
    private String classType; // "YOGA", "CARDIO", "STRENGTH"

    public GymClass(String className, String instructorName, String description,
                    int capacity, int durationMinutes, String classType) {
        this.className = className;
        this.instructorName = instructorName;
        this.description = description;
        this.capacity = capacity;
        this.durationMinutes = durationMinutes;
        this.classType = classType;
    }

    // Getters
    public int getClassId() { return classId; }
    public String getClassName() { return className; }
    public String getInstructorName() { return instructorName; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getClassType() { return classType; }

    // Setters
    public void setClassId(int classId) { this.classId = classId; }
    public void setClassName(String className) { this.className = className; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    @Override
    public String toString() {
        return "GymClass{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                ", instructor='" + instructorName + '\'' +
                ", type='" + classType + '\'' +
                ", capacity=" + capacity +
                ", duration=" + durationMinutes + " mins" +
                '}';
    }
}