package com.gym.utils;

import com.gym.domain.*;
import com.gym.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DataSeeder {

    private final UserRepository userRepo;
    private final ClassRepository classRepo;
    private final BookingRepository bookingRepo;
    private final ProgressRepository progressRepo;

    private final Random random = new Random();

    public DataSeeder(UserRepository userRepo,
                      ClassRepository classRepo,
                      BookingRepository bookingRepo,
                      ProgressRepository progressRepo) {

        this.userRepo = userRepo;
        this.classRepo = classRepo;
        this.bookingRepo = bookingRepo;
        this.progressRepo = progressRepo;
    }

    public void seedAll() {
        System.out.println("\n=== STARTING DATA SEEDING ===");

        List<User> trainers = createTrainers(10);
        List<User> members  = createMembers(50);
        List<GymClass> classes = createGymClasses(trainers);

        List<ClassSchedule> schedules = createSchedules(classes, 30);
        createRandomBookings(schedules, members);

        System.out.println("\n=== DATA SEEDING COMPLETE ===");
    }

    private List<User> createTrainers(int count) {
        List<User> trainers = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            User t = new User(
                    "trainer" + i,
                    "password123",
                    "trainer" + i + "@mail.com",
                    "TRAINER"
            );
            userRepo.save(t);
            progressRepoInitialization(t);
            trainers.add(t);
        }
        return trainers;
    }

    private List<User> createMembers(int count) {
        List<User> members = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            User m = new User(
                    "member" + i,
                    "password123",
                    "member" + i + "@mail.com",
                    "MEMBER"
            );
            userRepo.save(m);
            progressRepoInitialization(m);
            members.add(m);
        }
        return members;
    }

    private List<GymClass> createGymClasses(List<User> trainers) {

        String[] classTypes = {
                "HIIT", "STRENGTH", "CARDIO", "YOGA", "BOXING", "CORE",
                "CROSSFIT", "PILATES", "ENDURANCE", "KETTLEBELL", "SPINNING", "BOOTCAMP"
        };

        String[] descriptions = {
                "High intensity interval training.",
                "Strength and muscle building.",
                "Cardio endurance improvement.",
                "Relaxing flexibility training.",
                "High-energy punching and movement.",
                "Focused core workout.",
                "Advanced cross-training.",
                "Stretching and conditioning.",
                "Improve long-distance stamina.",
                "Strength with kettlebells.",
                "Cycling endurance workout.",
                "Military-style conditioning."
        };

        List<GymClass> classes = new ArrayList<>();

        for (int i = 0; i < classTypes.length; i++) {

            User randomTrainer = trainers.get(random.nextInt(trainers.size()));

            GymClass gc = new GymClass(
                    classTypes[i] + " Training",
                    randomTrainer.getUsername(),
                    descriptions[i],
                    20 + random.nextInt(15), // capacity 20–35
                    45 + random.nextInt(30), // duration 45–75 min
                    classTypes[i]
            );

            classRepo.saveClass(gc);
            classes.add(gc);
        }
        return classes;
    }

    private List<ClassSchedule> createSchedules(List<GymClass> classes, int daysAhead) {
        List<ClassSchedule> schedules = new ArrayList<>();

        LocalTime[] possibleTimes = {
                LocalTime.of(7, 0),
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                LocalTime.of(17, 0),
                LocalTime.of(19, 0)
        };

        for (GymClass gc : classes) {
            for (int day = 0; day < daysAhead; day++) {
                LocalDate date = LocalDate.now().plusDays(day);

                LocalTime start = possibleTimes[random.nextInt(possibleTimes.length)];
                LocalTime end   = start.plusMinutes(gc.getDurationMinutes());

                ClassSchedule schedule = new ClassSchedule(
                        0,
                        gc.getClassId(),
                        date,
                        start,
                        end,
                        gc.getCapacity()
                );

                classRepo.saveSchedule(schedule);
                schedules.add(schedule);
            }
        }
        return schedules;
    }

    private void createRandomBookings(List<ClassSchedule> schedules, List<User> members) {
        int bookingCount = 300;

        for (int i = 0; i < bookingCount; i++) {
            User member = members.get(random.nextInt(members.size()));
            ClassSchedule schedule = schedules.get(random.nextInt(schedules.size()));

            if (schedule.getAvailableSpots() <= 0) continue;

            Booking booking = new Booking(
                    member.getUserId(),
                    schedule.getScheduleId(),
                    "CONFIRMED"
            );

            bookingRepo.save(booking);
            schedule.setAvailableSpots(schedule.getAvailableSpots() - 1);
            classRepo.updateSchedule(schedule);

            // OPTIONAL: reward points for attending
            String classType = classRepo.findClassById(schedule.getClassId()).getClassType();
        }
    }

    private void progressRepoInitialization(User user) {
        String[] categories =
                {"CARDIO", "STRENGTH", "FLEXIBILITY", "ENDURANCE", "LEGS", "ARMS", "CORE"};

        for (String cat : categories) {
            FitnessProgress fp = new FitnessProgress(user.getUserId(), cat, 0);
            progressRepo.save(fp);
        }
    }
}

