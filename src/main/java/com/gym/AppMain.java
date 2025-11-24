package com.gym;

import com.gym.domain.User;
import com.gym.repository.UserRepository;
import com.gym.repository.sqlite.SqliteDatabaseManager;
import com.gym.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1) DB + servicios
        SqliteDatabaseManager.initializeDatabase();
        AppConfig.init();
        seedAdminUserIfMissing();

        // 2) Guardamos el stage principal
        SceneManager.setPrimaryStage(stage);

        // 3) Mostramos login
        SceneManager.switchTo("/views/login.fxml", "Gym Class Booking - Login");
    }

    private void seedAdminUserIfMissing() {
        UserRepository userRepo = AppConfig.getUserRepository();

        User admin = userRepo.findByUsername("admin");
        if (admin == null) {
            String rawPassword = "admin123";

            // Usa el mismo constructor que en register:
            // new User(username, password, email, role)
            User newAdmin = new User(
                    "admin",
                    rawPassword,
                    "admin@gym.com",
                    "ADMIN"
            );

            boolean saved = userRepo.save(newAdmin);
            if (saved) {
                System.out.println("Seeded default admin user: admin / admin123");
            } else {
                System.err.println("Failed to seed default admin user");
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
