package com.gym;

import com.gym.domain.User;
import com.gym.domain.GymClass;
import com.gym.domain.ClassSchedule;
import com.gym.repository.UserRepository;
import com.gym.service.ClassService;
import com.gym.utils.DataSeeder;
import com.gym.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // DB + services
        AppConfig.init();
        seedDefaultAdmin();

        // SSave principal stage
        SceneManager.setPrimaryStage(stage);

        // Show login
        SceneManager.switchTo("/views/login.fxml", "Gym Class Booking - Login");
    }

    private void seedDefaultAdmin() {
        UserRepository userRepo = AppConfig.getUserRepository();

        if (userRepo.findByUsername("admin") != null) {
            return;
        }
        User admin = new User(
                "admin",
                "admin123",
                "admin@gym.com",
                "ADMIN"
        );

        userRepo.save(admin);
        System.out.println("Default admin: admin / admin123");
    }


    public static void main(String[] args) {
        launch();
    }
}
