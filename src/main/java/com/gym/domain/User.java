package com.gym.domain;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String role;
    private String createdAt;

    // Constructor for new user
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Constructor for existing user from database
    public User(int userId, String username, String password, String email, String role, String createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getCreatedAt() { return createdAt; }

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
    public boolean isTrainer() {
        return "TRAINER".equalsIgnoreCase(role);
    }
    public boolean isMember() {
        return "MEMBER".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}