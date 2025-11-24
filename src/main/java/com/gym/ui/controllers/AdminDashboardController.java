package com.gym.ui.controllers;

import com.gym.AppConfig;
import com.gym.domain.User;
import com.gym.service.AuthService;
import com.gym.utils.SceneManager;
import com.gym.ui.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    private final AuthService authService = AppConfig.getAuthService();

    @FXML
    private void initialize() {
        // Show who is logged in
        User current = authService.getCurrentUser();
        if (current != null) {
            welcomeLabel.setText("Admin: " + current.getUsername());
        } else {
            welcomeLabel.setText("Admin dashboard");
        }
    }

    @FXML
    private void onManageClassesClicked() {
        showTodo("Manage classes");
        // Later: SceneManager.switchTo("/views/admin-classes.fxml", "Manage classes");
    }

    @FXML
    private void onManageSchedulesClicked() {
        showTodo("Manage schedules");
        // Later: SceneManager.switchTo("/views/admin-schedules.fxml", "Manage schedules");
    }

    @FXML
    private void onViewBookingsClicked() {
        showTodo("View all bookings");
        // Later: SceneManager.switchTo("/views/admin-bookings.fxml", "All bookings");
    }

    @FXML
    private void onViewMembersClicked() {
        showTodo("View members");
        // Later: SceneManager.switchTo("/views/admin-members.fxml", "Members");
    }

    @FXML
    private void onLogoutClicked() {
        authService.logout();
        SessionManager.clear();
        SceneManager.switchTo("/views/login.fxml", "Gym Login");
    }

    private void showTodo(String featureName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not implemented");
        alert.setHeaderText(null);
        alert.setContentText(featureName + " screen is not implemented yet.");
        alert.showAndWait();
    }
}