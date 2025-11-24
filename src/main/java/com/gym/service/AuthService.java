package com.gym.service;

import com.gym.domain.User;

public interface AuthService {
    User login(String username, String password);
    boolean register(String username, String password, String email, String role);
    void logout();
    User getCurrentUser();
}