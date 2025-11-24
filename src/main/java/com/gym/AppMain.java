package com.gym;

import com.gym.repository.sqlite.SqliteDatabaseManager;
import com.gym.ui.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1) DB + servicios
        SqliteDatabaseManager.initializeDatabase();
        AppConfig.init();

        // 2) Guardamos el stage principal
        SceneManager.setPrimaryStage(stage);

        // 3) Mostramos login
        SceneManager.switchTo("/views/login.fxml", "Gym Class Booking - Login");
    }

    public static void main(String[] args) {
        launch();
    }
}

