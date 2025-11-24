package com.gym.repository;

import com.gym.domain.FitnessProgress;
import java.util.List;

public interface ProgressRepository {
    boolean save(FitnessProgress progress);
    FitnessProgress findById(int progressId);
    FitnessProgress findByUserIdAndCategory(int userId, String category);
    List<FitnessProgress> findByUserId(int userId);
    List<FitnessProgress> findAll();
    boolean update(FitnessProgress progress);
    boolean delete(int progressId);
}