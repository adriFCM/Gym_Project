package com.gym.ui.controllers;

import com.gym.AppConfig;
import com.gym.domain.Booking;
import com.gym.domain.ClassSchedule;
import com.gym.domain.GymClass;
import com.gym.repository.BookingRepository;
import com.gym.repository.ClassRepository;
import com.gym.service.AuthService;
import com.gym.utils.SceneManager;
import com.gym.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminAnalyticsController {

    // Charts
    @FXML
    private PieChart bookingsByStatusChart;

    @FXML
    private BarChart<String, Number> bookingsPerClassChart;

    // Tables (left: status)
    @FXML
    private TableView<StatusSummary> statusSummaryTable;

    @FXML
    private TableColumn<StatusSummary, String> statusColumn;

    @FXML
    private TableColumn<StatusSummary, Number> statusCountColumn;

    // Tables (right: type)
    @FXML
    private TableView<ClassSummary> classSummaryTable;

    @FXML
    private TableColumn<ClassSummary, String> classColumn;      // shows type
    @FXML
    private TableColumn<ClassSummary, Number> classCountColumn; // count

    private final AuthService authService = AppConfig.getAuthService();
    private final BookingRepository bookingRepository = AppConfig.getBookingRepository();
    private final ClassRepository classRepository = AppConfig.getClassRepository();

    @FXML
    public void initialize() {
        // Wire table columns to fields in the summary classes
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        classColumn.setCellValueFactory(new PropertyValueFactory<>("classLabel")); // type label
        classCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        populateBookingsByStatus();
        populateBookingsByType();
    }

    /**
     * Pie + table: how many bookings are in each status (CONFIRMED, CANCELLED, ATTENDED, etc.)
     * Uses real Booking data via BookingRepository.
     */
    private void populateBookingsByStatus() {
        bookingsByStatusChart.getData().clear();
        statusSummaryTable.getItems().clear();

        List<Booking> bookings = bookingRepository.findAll();
        if (bookings == null || bookings.isEmpty()) {
            return;
        }

        Map<String, Long> countsByStatus = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> {
                            String s = booking.getStatus();
                            return (s == null || s.isBlank()) ? "UNKNOWN" : s.toUpperCase();
                        },
                        Collectors.counting()
                ));

        // Fill chart
        countsByStatus.forEach((status, count) ->
                bookingsByStatusChart.getData().add(new PieChart.Data(status, count))
        );

        // Fill table
        ObservableList<StatusSummary> rows = FXCollections.observableArrayList();
        countsByStatus.forEach((status, count) ->
                rows.add(new StatusSummary(status, count.intValue()))
        );
        statusSummaryTable.setItems(rows);
    }

    /**
     * Bar chart + table: how many (non-cancelled) bookings each training TYPE has.
     * Types are derived from the real GymClass names:
     *  - contains "yoga"   -> Yoga
     *  - contains "cardio" or "cycle"/"cycling" -> Cardio
     *  - contains "hiit" or "bootcamp"/"blast"  -> HIIT
     *  - otherwise                               -> Strength
     */
    private void populateBookingsByType() {
        bookingsPerClassChart.getData().clear();
        classSummaryTable.getItems().clear();

        List<Booking> bookings = bookingRepository.findAll();
        if (bookings == null || bookings.isEmpty()) {
            return;
        }

        Map<String, Integer> bookingsPerType = new HashMap<>();

        for (Booking booking : bookings) {
            // Skip cancelled if you only care about active/attended
            String status = booking.getStatus();
            if (status != null && status.equalsIgnoreCase("CANCELLED")) {
                continue;
            }

            // schedule -> class
            ClassSchedule schedule = classRepository.findScheduleById(booking.getScheduleId());
            if (schedule == null) {
                continue;
            }

            GymClass gymClass = classRepository.findClassById(schedule.getClassId());
            if (gymClass == null) {
                continue;
            }

            String name = gymClass.getClassName();   // uses actual DB class name
            if (name == null) {
                name = "";
            }

            String typeLabel = resolveTypeFromName(name);
            bookingsPerType.merge(typeLabel, 1, Integer::sum);
        }

        if (bookingsPerType.isEmpty()) {
            return;
        }

        // Fill chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bookings");

        bookingsPerType.forEach((typeLabel, count) ->
                series.getData().add(new XYChart.Data<>(typeLabel, count))
        );
        bookingsPerClassChart.getData().add(series);

        // Fill table
        ObservableList<ClassSummary> rows = FXCollections.observableArrayList();
        bookingsPerType.forEach((typeLabel, count) ->
                rows.add(new ClassSummary(typeLabel, count))
        );
        classSummaryTable.setItems(rows);
    }

    /**
     * Map a class name (e.g. "Zen Flow Yoga", "Bootcamp Blast") into one of
     * the four training types your app uses: Yoga, Strength, Cardio, HIIT.
     */
    private String resolveTypeFromName(String className) {
        String lower = className.toLowerCase();

        if (lower.contains("yoga")) {
            return "Yoga";
        }

        if (lower.contains("cardio") || lower.contains("cycle") || lower.contains("cycling")) {
            return "Cardio";
        }

        if (lower.contains("hiit") || lower.contains("bootcamp") || lower.contains("blast")) {
            return "HIIT";
        }

        // default bucket: strength / weight-based
        return "Strength";
    }

    @FXML
    private void onBackToDashboardClicked() {
        SceneManager.switchTo("/views/admin-dashboard.fxml", "Admin dashboard");
    }

    @FXML
    private void onLogoutClicked() {
        authService.logout();
        SessionManager.clear();
        SceneManager.switchTo("/views/login.fxml", "Gym Login");
    }

    // ==== Helper classes for tables ====

    public static class StatusSummary {
        private final String status;
        private final Integer count;

        public StatusSummary(String status, Integer count) {
            this.status = status;
            this.count = count;
        }

        public String getStatus() {
            return status;
        }

        public Integer getCount() {
            return count;
        }
    }

    public static class ClassSummary {
        private final String classLabel; // here: type label (Yoga / Strength / Cardio / HIIT)
        private final Integer count;

        public ClassSummary(String classLabel, Integer count) {
            this.classLabel = classLabel;
            this.count = count;
        }

        public String getClassLabel() {
            return classLabel;
        }

        public Integer getCount() {
            return count;
        }
    }
}
