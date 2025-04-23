package com.example.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ViewRecipientsPageController implements Initializable {

    @FXML
    private TableView<RecipientInfo> recipientsTable;
    @FXML
    private TableColumn<RecipientInfo, String> nameColumn;
    @FXML
    private TableColumn<RecipientInfo, String> bloodTypeColumn;
    @FXML
    private TableColumn<RecipientInfo, String> contactColumn;
    @FXML
    private Button Goback;

    @FXML
    void GoBack(MouseEvent event) throws IOException {
        Stage stage = (Stage) Goback.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WelcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 350); // Set dimensions to 600x400
        stage.setTitle("Welcome Page");
        stage.setScene(scene);
    }

    private ObservableList<RecipientInfo> recipientData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bloodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));

        recipientsTable.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                stage.setWidth(700);
                stage.setHeight(600);
            }
        });

        loadRecipients();
    }

    private void loadRecipients() {
        recipientData = FXCollections.observableArrayList();
        String url = "jdbc:mysql://localhost:3306/blooddonationmanagement";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement stmt = connection.createStatement()) {
            String query = "SELECT name, BloodType, contact FROM recipient ORDER BY BloodType";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                recipientData.add(new RecipientInfo(
                        rs.getString("name"),
                        rs.getString("BloodType"),
                        rs.getString("contact")
                ));
            }
            recipientsTable.setItems(recipientData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class RecipientInfo {
        private final String name;
        private final String bloodType;
        private final String contact;

        RecipientInfo(String name, String bloodType, String contact) {
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
}