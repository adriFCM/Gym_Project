package com.gym.ui.controllers;

import com.gym.AppConfig;
import com.gym.domain.User;
import com.gym.service.AuthService;
import com.gym.ui.utils.SceneManager;
import com.gym.ui.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = AppConfig.getAuthService();

    @FXML
    private void onLoginClicked() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user == null || user.isBlank() || pass == null || pass.isBlank()) {
            errorLabel.setText("Please enter username and password");
            return;
        }

        try {
            User logged = authService.login(user, pass);
            SessionManager.setCurrentUser(logged);

            switch (logged.getRole()) {
                case "ADMIN" -> SceneManager.switchTo("/views/admin-dashboard.fxml",
                        "Admin Dashboard");
                case "INSTRUCTOR" -> SceneManager.switchTo("/views/member-dashboard.fxml",
                        "Instructor Schedule"); // luego tendréis su propia vista
                case "MEMBER" -> SceneManager.switchTo("/views/member-dashboard.fxml",
                        "Member Dashboard");
                default -> {
                    errorLabel.setText("Unknown role: " + logged.getRole());
                    SessionManager.clear();
                }
            }

        } catch (Exception e) {
            // Aquí AuthServiceImpl puede lanzar excepción si login falla
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onRegisterClicked() {
        // más adelante: abrir pantalla de registro si queréis
        errorLabel.setText("Register not implemented yet");
    }
}
