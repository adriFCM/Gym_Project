package com.gym.gym_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1) Make sure views/login.fxml exists under src/main/resources/views/
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("/views/login.fxml"));
        stage.setTitle("GymClass");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

