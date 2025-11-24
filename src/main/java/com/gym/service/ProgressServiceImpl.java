package com.gym.service;

import com.gym.domain.FitnessProgress;
import com.gym.repository.ProgressRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;
    private final Map<String, Map<String, Integer>> pointSystem;

    public ProgressServiceImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
        this.pointSystem = initializePointSystem();
    }

    private Map<String, Map<String, Integer>> initializePointSystem() {
        Map<String, Map<String, Integer>> system = new HashMap<>();

        // HIIT classes
        Map<String, Integer> hiitPoints = new HashMap<>();
        hiitPoints.put("CARDIO", 70);
        hiitPoints.put("STRENGTH", 60);
        hiitPoints.put("ENDURANCE", 50);
        hiitPoints.put("LEGS", 40);
        system.put("HIIT", hiitPoints);

        // YOGA classes
        Map<String, Integer> yogaPoints = new HashMap<>();
        yogaPoints.put("FLEXIBILITY", 50);
        yogaPoints.put("CORE", 30);
        yogaPoints.put("STRENGTH", 20);
        system.put("YOGA", yogaPoints);

        // STRENGTH classes
        Map<String, Integer> strengthPoints = new HashMap<>();
        strengthPoints.put("STRENGTH", 80);
        strengthPoints.put("ARMS", 60);
        strengthPoints.put("LEGS", 60);
        strengthPoints.put("CORE", 40);
        system.put("STRENGTH", strengthPoints);

        // CARDIO classes
        Map<String, Integer> cardioPoints = new HashMap<>();
        cardioPoints.put("CARDIO", 80);
        cardioPoints.put("ENDURANCE", 70);
        cardioPoints.put("LEGS", 50);
        system.put("CARDIO", cardioPoints);

        return system;
    }

    @Override
    public void initializeUserProgress(int userId) {
        String[] categories = {"CARDIO", "STRENGTH", "FLEXIBILITY", "ENDURANCE", "LEGS", "ARMS", "CORE"};

        for (String category : categories) {
            // Check if progress already exists
            FitnessProgress existing = progressRepository.findByUserIdAndCategory(userId, category);
            if (existing == null) {
                FitnessProgress progress = new FitnessProgress(userId, category, 0);
                progressRepository.save(progress);
            }
        }
        System.out.println("Initialized progress for user " + userId);
    }
    @Override
    public boolean awardPointsForClass(int userId, String classType) {
        if (classType == null || !pointSystem.containsKey(classType)) {
            System.err.println("Invalid class type: " + classType);
            return false;
        }

        Map<String, Integer> pointsToAward = pointSystem.get(classType);

        for (Map.Entry<String, Integer> entry : pointsToAward.entrySet()) {
            String category = entry.getKey();
            int points = entry.getValue();

            // Get or create progress for this category
            FitnessProgress progress = progressRepository.findByUserIdAndCategory(userId, category);

            if (progress == null) {
                // Initialize if doesn't exist
                progress = new FitnessProgress(userId, category, 0);
                progressRepository.save(progress);
            }

            // Award points
            progress.addPoints(points);
            progressRepository.update(progress);

            System.out.println("Awarded " + points + " points to " + category);
        }

        return true;
    }
    @Override
    public FitnessProgress getUserProgressByCategory(int userId, String category) {
        return progressRepository.findByUserIdAndCategory(userId, category);
    }
    @Override
    public List<FitnessProgress> getAllUserProgress(int userId) {
        return progressRepository.findByUserId(userId);
    }
    @Override
    public Map<String, Integer> getPointsForClassType(String classType) {
        return pointSystem.getOrDefault(classType, new HashMap<>());
    }
}