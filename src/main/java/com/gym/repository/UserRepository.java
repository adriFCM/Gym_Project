package com.gym.repository;

import com.gym.domain.User;
import java.util.List;

public interface UserRepository {
    boolean save(User user);

    // Read
    User findById(int userId);
    User findByUsername(String username);
    List<User> findAll();

    boolean update(User user);
    boolean delete(int userId);

    // Authentication
    User validateLogin(String username, String password);
}