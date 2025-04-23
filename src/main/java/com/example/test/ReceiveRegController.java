package com.example.test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.List;

public class ReceiveRegController {

    private Connection connection;

    public static class DonorInfo {
        private final StringProperty name;
        private final StringProperty bloodType;
        private final StringProperty contact;

        DonorInfo(String name, String bloodType, String contact) {
            this.name = new SimpleStringProperty(name);
            this.bloodType = new SimpleStringProperty(bloodType);
            this.contact = new SimpleStringProperty(contact);
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public String getBloodType() {
            return bloodType.get();
        }

        public StringProperty bloodTypeProperty() {
            return bloodType;
        }

        public String getContact() {
            return contact.get();
        }

        public StringProperty contactProperty() {
            return contact;
        }
    }

    public ReceiveRegController() {
        try {
            String url = "jdbc:mysql://localhost:3306/"; // Database URL without database name
            String user = "root";                       // Database username
            String password = "root";                   // Database password
            this.connection = DriverManager.getConnection(url, user, password);
            createDatabaseAndTable(url, user, password); // Create database and table if they do not exist
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private ImageView logo;

    @FXML
    private Button Goback;

    @FXML
    void GoBack(MouseEvent event) throws IOException {
        Stage stage = (Stage) Goback.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WelcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 350);
        stage.setTitle("Blood Finder Application");
        stage.setScene(scene);
    }

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
                String createRecipientTable = "CREATE TABLE IF NOT EXISTS recipient ("
                        + "ID_number VARCHAR(20) PRIMARY KEY, "
                        + "name VARCHAR(100), "
                        + "DOB DATE, "
                        + "BloodType VARCHAR(3), "
                        + "contact VARCHAR(15))";
                tableStatement.executeUpdate(createRecipientTable);

                String createDonorTable = "CREATE TABLE IF NOT EXISTS donor ("
                        + "ID_number VARCHAR(20) PRIMARY KEY, "
                        + "name VARCHAR(100), "
                        + "DOB DATE, "
                        + "BloodType VARCHAR(3), "
                        + "contact VARCHAR(15))";
                tableStatement.executeUpdate(createDonorTable);
            }
        }
    }

    private List<DonorInfo> findMatchingDonors(String recipientBloodType) throws SQLException {
        List<DonorInfo> matchingDonors = new ArrayList<>();
        String query = "SELECT name, BloodType, contact FROM donor WHERE BloodType = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, recipientBloodType);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                matchingDonors.add(new DonorInfo(
                        rs.getString("name"),
                        rs.getString("BloodType"),
                        rs.getString("contact")
                ));
            }
        }
        return matchingDonors;
    }

    @FXML
    private void submit(MouseEvent event) throws IOException, SQLException {
        String name = nameField.getText();
        String id = idField.getText();
        String dob = dobPicker.getValue() != null ? dobPicker.getValue().toString() : "";
        String bloodType = bloodTypeComboBox.getValue();
        String contact = Contact.getText();

        String checkQuery = "SELECT COUNT(*) FROM recipient WHERE ID_number = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, id);
            ResultSet rs = checkStatement.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // ID already exists
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate ID");
                alert.setContentText("Recipient with this ID already registered.");
                alert.showAndWait();
                return;
            }
        }

        String query = "INSERT INTO recipient (ID_number, name, DOB, BloodType, contact) VALUES(?,?,?,?,?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, dob);
            preparedStatement.setString(4, bloodType);
            preparedStatement.setString(5, contact);
            preparedStatement.executeUpdate();
        }

        List<DonorInfo> matchingDonors = findMatchingDonors(bloodType);
        ObservableList<DonorInfo> data = FXCollections.observableArrayList(matchingDonors);

        // Load the next page and pass the data to the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/test/RecRegSuc.fxml"));
        AnchorPane pane = loader.load();
        RecRegSuc controller = loader.getController();
        controller.populateTable(data);

        Stage stage = (Stage) submitButton.getScene().getWindow();
        Scene scene = new Scene(pane, 600, 400);  // Set the dimensions here to maintain size
        stage.setScene(scene);
        stage.setTitle("Registration Successful");
        stage.show();
    }
}