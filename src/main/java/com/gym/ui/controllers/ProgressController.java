package com.gym.ui.controllers;

import com.gym.AppConfig;
import com.gym.domain.FitnessProgress;
import com.gym.domain.ClassSchedule;
import com.gym.domain.GymClass;
import com.gym.service.ProgressService;
import com.gym.service.BookingService;
import com.gym.service.ClassService;
import com.gym.ui.utils.SessionManager;
import com.gym.utils.SceneManager;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProgressController {

    // NEW — circular progress indicator
    @FXML private ProgressIndicator monthlyIndicator;
    @FXML private Label monthlyPercentLabel;
    @FXML private Label monthlyGoalLabel;

    // Table
    @FXML private TableView<RecentRow> recentTable;
    @FXML private TableColumn<RecentRow, String> recentClassColumn;
    @FXML private TableColumn<RecentRow, String> recentDateColumn;
    @FXML private TableColumn<RecentRow, String> recentFocusColumn;
    @FXML private TableColumn<RecentRow, Number> recentPointsColumn;

    @FXML private Label messageLabel;

    private ProgressService progressService;
    private BookingService bookingService;
    private ClassService classService;

    private final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private void initialize() {
        progressService = AppConfig.getProgressService();
        bookingService  = AppConfig.getBookingService();
        classService    = AppConfig.getClassService();

        // Table setup
        recentClassColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().className()));
        recentDateColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().date()));
        recentFocusColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().focus()));
        recentPointsColumn.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().points()));

        loadData();
    }

    private void loadData() {
        var user = SessionManager.getCurrentUser();
        if (user == null) {
            messageLabel.setText("You must be logged in.");
            return;
        }

        // Ensure user progress exists
        progressService.initializeUserProgress(user.getUserId());

        // Load all category progress
        List<FitnessProgress> stats =
                progressService.getAllUserProgress(user.getUserId());

        if (stats.isEmpty()) {
            messageLabel.setText("No progress data available.");
            return;
        }

        // --- NEW: Monthly fitness stats ---
        int totalThisMonth = stats.stream()
                .mapToInt(FitnessProgress::getPointsThisMonth)
                .sum();

        int monthlyGoal = stats.get(0).getMonthlyGoal();  // same for all categories normally

        double progress = (monthlyGoal == 0)
                ? 0
                : (double) totalThisMonth / monthlyGoal;

        monthlyIndicator.setProgress(progress);
        monthlyPercentLabel.setText((int) (progress * 100) + "%");
        monthlyGoalLabel.setText("Goal: " + monthlyGoal + " pts");

        // Load user's recently completed classes
        loadRecentClasses(user.getUserId());
    }

    private void loadRecentClasses(int userId) {
        List<RecentRow> rows = bookingService.getUserBookings(userId).stream()
                .filter(b -> b.isConfirmed())
                .map(b -> {
                    ClassSchedule s = classService.getScheduleById(b.getScheduleId());
                    if (s == null || s.getEndTime() == null) return null;

                    LocalDateTime classEnd = LocalDateTime.of(
                            s.getScheduledDate(), s.getEndTime());

                    if (classEnd.isAfter(LocalDateTime.now())) return null;

                    GymClass gc = classService.getClassById(s.getClassId());
                    if (gc == null) return null;

                    int pts = 10; // TODO: replace with actual point system

                    return new RecentRow(
                            gc.getClassName(),
                            DATE_FORMATTER.format(s.getScheduledDate()),
                            gc.getClassType(),
                            pts
                    );
                })
                .filter(r -> r != null)
                .toList();

        recentTable.setItems(FXCollections.observableArrayList(rows));
    }

    @FXML
    private void onBackClicked() {
        SceneManager.switchTo("/views/member-dashboard.fxml", "Member dashboard");
    }

    public record RecentRow(
            String className,
            String date,
            String focus,
            int points
    ) {}
}
