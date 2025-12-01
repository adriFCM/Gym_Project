package com.gym.service;

import com.gym.domain.FitnessProgress;
import com.gym.repository.ProgressRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;
    private final Map<String, Map<String, Integer>> pointSystem;

    public ProgressServiceImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
        this.pointSystem = initializePointSystem();
    }

    private void resetIfNewMonth(FitnessProgress fp) {
        if (fp.getLastUpdated() == null) return;

        LocalDate last = fp.getLastUpdated();
        LocalDate now = LocalDate.now();

        if (last.getMonthValue() != now.getMonthValue() ||
                last.getYear() != now.getYear()) {
            fp.setPointsThisMonth(0);
            progressRepository.update(fp);
        }
    }


    private Map<String, Map<String, Integer>> initializePointSystem() {
        Map<String, Map<String, Integer>> system = new HashMap<>();

        // HIIT classes
        Map<String, Integer> hiitPoints = new HashMap<>();
        hiitPoints.put("CARDIO", 20);
        hiitPoints.put("STRENGTH", 17);
        hiitPoints.put("ENDURANCE", 14);
        hiitPoints.put("LEGS", 10);
        system.put("HIIT", hiitPoints);

        // YOGA classes
        Map<String, Integer> yogaPoints = new HashMap<>();
        yogaPoints.put("FLEXIBILITY", 15);
        yogaPoints.put("CORE", 9);
        yogaPoints.put("STRENGTH", 6);
        system.put("YOGA", yogaPoints);

        // STRENGTH classes
        Map<String, Integer> strengthPoints = new HashMap<>();
        strengthPoints.put("STRENGTH", 20);
        strengthPoints.put("ARMS", 15);
        strengthPoints.put("LEGS", 15);
        strengthPoints.put("CORE", 10);
        system.put("STRENGTH", strengthPoints);

        // CARDIO classes
        Map<String, Integer> cardioPoints = new HashMap<>();
        cardioPoints.put("CARDIO", 20);
        cardioPoints.put("ENDURANCE", 18);
        cardioPoints.put("LEGS", 14);
        system.put("CARDIO", cardioPoints);

        return system;
    }


    @Override
    public void initializeUserProgress(int userId) {
        String[] categories = {"CARDIO", "STRENGTH", "FLEXIBILITY", "ENDURANCE", "LEGS", "ARMS", "CORE"};

        for (String category : categories) {
            // Check if progress already exists
            FitnessProgress existing = progressRepository.findByUserIdAndCategory(userId, category);
            if (existing.getLastUpdated() != null &&
                    existing.getLastUpdated().getMonthValue() != LocalDate.now().getMonthValue()) {
                existing.setPointsThisMonth(0);
                progressRepository.update(existing);
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
        List<FitnessProgress> list = progressRepository.findByUserId(userId);

        for (FitnessProgress fp : list) {
            resetIfNewMonth(fp);
        }

        return list;
    }

    @Override
    public Map<String, Integer> getPointsForClassType(String classType) {
        return pointSystem.getOrDefault(classType, new HashMap<>());
    }
}