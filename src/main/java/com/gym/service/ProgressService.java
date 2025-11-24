package com.gym.service;

import com.gym.domain.FitnessProgress;
import java.util.List;
import java.util.Map;

public interface ProgressService {
    void initializeUserProgress(int userId);
    boolean awardPointsForClass(int userId, String classType);

    FitnessProgress getUserProgressByCategory(int userId, String category);
    List<FitnessProgress> getAllUserProgress(int userId);

    Map<String, Integer> getPointsForClassType(String classType);
}