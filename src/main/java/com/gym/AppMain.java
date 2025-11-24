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
        // seedAdminUserIfMissing();
        seedDefaultUsers();

        // 2) Guardamos el stage principal
        SceneManager.setPrimaryStage(stage);

        // 3) Mostramos login
        SceneManager.switchTo("/views/login.fxml", "Gym Class Booking - Login");
    }

    private void seedDefaultUsers() {
        UserRepository userRepo = AppConfig.getUserRepository();

        seedUserIfMissing(userRepo, "admin",  "admin123",  "ADMIN");
        seedUserIfMissing(userRepo, "member", "member123", "MEMBER");
        seedUserIfMissing(userRepo, "trainer","trainer123","TRAINER");
    }

    private void seedUserIfMissing(UserRepository repo,
                                   String username,
                                   String rawPassword,
                                   String role) {
        User existing = repo.findByUsername(username);
        if (existing != null) return;

        User u = new User(username, rawPassword, username + "@gym.com", role);
        boolean ok = repo.save(u);
        if (ok) System.out.println("Seeded " + role + " user: " + username + " / " + rawPassword);
    }

    public static void main(String[] args) {
        launch();
    }
}
