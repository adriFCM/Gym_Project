package com.gym.service;

import com.gym.domain.User;
import com.gym.repository.UserRepository;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private User currentUser = null;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty()) {
            System.err.println("Username cannot be empty");
            return null;
        }
        if (password == null || password.isEmpty()) {
            System.err.println("Password cannot be empty");
            return null;
        }
        User user = userRepository.validateLogin(username, password);

        if (user != null) {
            currentUser = user;
            System.out.println("Login successful: " + user.getUsername());
        } else {
            System.err.println("Invalid username or password");
        }

        return user;
    }

    @Override
    public boolean register(String username, String password, String email, String role) {
        if (username == null || username.length() < 3) {
            System.err.println("Username must be at least 3 characters");
            return false;
        }
        if (password == null || password.length() < 6) {
            System.err.println("Password must be at least 6 characters");
            return false;
        }
        if (email == null || !email.contains("@")) {
            System.err.println("Invalid email format");
            return false;
        }
        if (!role.equals("ADMIN") && !role.equals("TRAINER") && !role.equals("MEMBER")) {
            System.err.println("Invalid role. Must be ADMIN, TRAINER, or MEMBER");
            return false;
        }
        if (userRepository.findByUsername(username) != null) {
            System.err.println("Username already taken");
            return false;
        }
        User newUser = new User(username, password, email, role);
        boolean saved = userRepository.save(newUser);

        if (saved) {
            System.out.println("Registration successful: " + username);
        }
        return saved;
    }
    @Override
    public void logout() {
        if (currentUser != null) {
            System.out.println("Logged out: " + currentUser.getUsername());
            currentUser = null;
        }
    }
    @Override
    public User getCurrentUser() {
        return currentUser;
    }
}