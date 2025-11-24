package com.gym.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load view: " + fxmlPath + " - " + e.getMessage());
        }
    }
}

