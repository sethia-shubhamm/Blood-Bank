package com.example.test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DonorRegController {

    private Connection connection;
    private Scanner scanner;

    // RecipientInfo class to hold recipient information
    public static class RecipientInfo {
        private final String name;
        private final String bloodType;
        private final String contact;

        public RecipientInfo(String name, String bloodType, String contact) {
            this.name = name;
            this.bloodType = bloodType;
            this.contact = contact;
        }

        public String getName() {
            return name;
        }

        public String getBloodType() {
            return bloodType;
        }

        public String getContact() {
            return contact;
        }
    }

    // No-argument constructor
    public DonorRegController() {
        try {
            String url = "jdbc:mysql://localhost:3306/";
            String user = "root";
            String password = "root";
            this.connection = DriverManager.getConnection(url, user, password);
            createDatabaseAndTable(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DonorRegController(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    @FXML
    private TextField nameField;

    @FXML
    private TextField idField;

    @FXML
    private TextField Contact;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private ComboBox<String> bloodTypeComboBox;

    @FXML
    private Button submitButton;

    @FXML
    private Button Goback;

    @FXML
    private ImageView logo;

    @FXML
    public void initialize() {
        bloodTypeComboBox.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        logo.setVisible(true);
    }

    private void createDatabaseAndTable(String url, String user, String password) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createDatabase = "CREATE DATABASE IF NOT EXISTS blooddonationmanagement";
            statement.executeUpdate(createDatabase);

            connection = DriverManager.getConnection(url + "blooddonationmanagement", user, password);

            try (Statement tableStatement = connection.createStatement()) {
                String createDonorTable = "CREATE TABLE IF NOT EXISTS donor (" +
                        "ID_number VARCHAR(20) PRIMARY KEY, " +
                        "name VARCHAR(100), " +
                        "DOB DATE, " +
                        "BloodType VARCHAR(3), " +
                        "contact VARCHAR(15))";
                tableStatement.executeUpdate(createDonorTable);

                String createRecipientTable = "CREATE TABLE IF NOT EXISTS recipient (" +
                        "ID_number VARCHAR(20) PRIMARY KEY, " +
                        "name VARCHAR(100), " +
                        "DOB DATE, " +
                        "BloodType VARCHAR(3), " +
                        "contact VARCHAR(15))";
                tableStatement.executeUpdate(createRecipientTable);
            }
        }
    }

    private List<String> getCompatibleBloodTypes(String donorBloodType) {
        List<String> compatibleTypes = new ArrayList<>();
        switch (donorBloodType) {
            case "O-":
                compatibleTypes.add("O-");
                compatibleTypes.add("O+");
                compatibleTypes.add("A-");
                compatibleTypes.add("A+");
                compatibleTypes.add("B-");
                compatibleTypes.add("B+");
                compatibleTypes.add("AB-");
                compatibleTypes.add("AB+");
                break;
            case "O+":
                compatibleTypes.add("O+");
                compatibleTypes.add("A+");
                compatibleTypes.add("B+");
                compatibleTypes.add("AB+");
                break;
            case "A-":
                compatibleTypes.add("A-");
                compatibleTypes.add("A+");
                compatibleTypes.add("AB-");
                compatibleTypes.add("AB+");
                break;
            case "A+":
                compatibleTypes.add("A+");
                compatibleTypes.add("AB+");
                break;
            case "B-":
                compatibleTypes.add("B-");
                compatibleTypes.add("B+");
                compatibleTypes.add("AB-");
                compatibleTypes.add("AB+");
                break;
            case "B+":
                compatibleTypes.add("B+");
                compatibleTypes.add("AB+");
                break;
            case "AB-":
                compatibleTypes.add("AB-");
                compatibleTypes.add("AB+");
                break;
            case "AB+":
                compatibleTypes.add("AB+");
                break;
        }
        return compatibleTypes;
    }

    private List<RecipientInfo> findMatchingRecipients(String donorBloodType) throws SQLException {
        List<RecipientInfo> matchingRecipients = new ArrayList<>();
        List<String> compatibleTypes = getCompatibleBloodTypes(donorBloodType);

        String placeholders = String.join(",", java.util.Collections.nCopies(compatibleTypes.size(), "?"));
        String query = "SELECT name, BloodType, contact FROM recipient WHERE BloodType IN (" + placeholders + ")";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < compatibleTypes.size(); i++) {
                stmt.setString(i + 1, compatibleTypes.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                matchingRecipients.add(new RecipientInfo(
                        rs.getString("name"),
                        rs.getString("BloodType"),
                        rs.getString("contact")
                ));
            }
        }
        return matchingRecipients;
    }

    @FXML
    void GoBack(MouseEvent event) throws IOException {
        Stage stage = (Stage) Goback.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WelcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 350);
        stage.setTitle("Welcome");
        stage.setScene(scene);
    }

    @FXML
    private void submit(MouseEvent event) throws IOException, SQLException {
        String name = nameField.getText();
        String id = idField.getText();
        LocalDate dob = dobPicker.getValue();
        String bloodType = bloodTypeComboBox.getValue();
        String contact = Contact.getText();

        // Check if DOB is not null and age is 18 or older
        if (dob == null || Period.between(dob, LocalDate.now()).getYears() < 18) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Age");
            alert.setHeaderText(null);
            alert.setContentText("Donor must be 18 years or older.");
            alert.showAndWait();
            return;
        }

        // Check if ID already exists
        String checkQuery = "SELECT COUNT(*) FROM donor WHERE ID_number = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, id);
            ResultSet rs = checkStatement.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate ID");
                alert.setHeaderText(null);
                alert.setContentText("A donor with the same ID already exists.");
                alert.showAndWait();
                return;
            }
        }

        // Insert donor data
        String query = "INSERT INTO donor (ID_number, name, DOB, BloodType, contact) VALUES(?,?,?,?,?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, dob.toString());
            preparedStatement.setString(4, bloodType);
            preparedStatement.setString(5, contact);
            preparedStatement.executeUpdate();
        }

        // Find matching recipients
        List<RecipientInfo> matchingRecipients = findMatchingRecipients(bloodType);

        // Load the FXML for successful registration
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DonRegSuc.fxml"));
        AnchorPane root = loader.load();

        // Get the controller and set the matching recipients
        DonRegSuc successController = loader.getController();
        successController.setMatchingRecipients(matchingRecipients);

        // Show the new scene
        Stage stage = (Stage) submitButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Registration Successful");
        stage.setScene(scene);
        stage.show();
    }
}