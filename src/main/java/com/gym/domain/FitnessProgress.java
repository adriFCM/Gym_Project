package com.gym.domain;

import java.time.LocalDate;

public class FitnessProgress {
    private int progressId;
    private int userId;
    private String category; // "CARDIO", "STRENGTH", "FLEXIBILITY", "ENDURANCE", "LEGS", "ARMS", "CORE"
    private int totalPoints;
    private LocalDate lastUpdated;

    public FitnessProgress(int userId, String category, int totalPoints) {
        this.userId = userId;
        this.category = category;
        this.totalPoints = totalPoints;
        this.lastUpdated = LocalDate.now();
    }

    // Constructor for existing progress from database
    public FitnessProgress(int progressId, int userId, String category,
                           int totalPoints, LocalDate lastUpdated) {
        this.progressId = progressId;
        this.userId = userId;
        this.category = category;
        this.totalPoints = totalPoints;
        this.lastUpdated = lastUpdated;
    }

    // Getters
    public int getProgressId() { return progressId; }
    public int getUserId() { return userId; }
    public String getCategory() { return category; }
    public int getTotalPoints() { return totalPoints; }
    public LocalDate getLastUpdated() { return lastUpdated; }

    // Setters
    public void setProgressId(int progressId) { this.progressId = progressId; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }

    public void addPoints(int points) {
        this.totalPoints += points;
        this.lastUpdated = LocalDate.now();
    }
    public int getLevel() {
        // Every 100 points = 1 level
        return totalPoints / 100;
    }
    @Override
    public String toString() {
        return "FitnessProgress{" +
                "category='" + category + '\'' +
                ", points=" + totalPoints +
                ", level=" + getLevel() +
                '}';
    }
}