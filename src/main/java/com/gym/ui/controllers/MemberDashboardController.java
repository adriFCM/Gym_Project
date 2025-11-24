package com.gym.ui.controllers;

import com.gym.AppConfig;
import com.gym.domain.User;
import com.gym.service.BookingService;
import com.gym.service.ProgressService;
import com.gym.ui.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class MemberDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<?> classesTable;   // luego definimos columnas
    @FXML private TableView<?> bookingsTable;  // igual
    // más nodos: progress bars, etc.

    private final BookingService bookingService = AppConfig.getBookingService();
    private final ProgressService progressService = AppConfig.getProgressService();

    @FXML
    private void initialize() {
        User current = SessionManager.getCurrentUser();
        if (current != null) {
            welcomeLabel.setText("Welcome, " + current.getUsername() + "!");
            // TODO: cargar bookings y progreso
        } else {
            welcomeLabel.setText("No active user session");
        }
    }

    // más métodos: onBookClicked, onCancelBooking, etc.
}
