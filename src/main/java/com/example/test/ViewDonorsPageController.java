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





public class ViewDonorsPageController implements Initializable {

    @FXML
    private TableView<DonorInfo> donorsTable;
    @FXML
    private TableColumn<DonorInfo, String> nameColumn;
    @FXML
    private TableColumn<DonorInfo, String> bloodTypeColumn;
    @FXML
    private TableColumn<DonorInfo, String> contactColumn;
    @FXML
    private Button Back;

    private ObservableList<DonorInfo> donorData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bloodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        loadDonors();
    }
    @FXML
    void Goback(MouseEvent event) throws IOException{
        Stage stage = (Stage) Back.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WelcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 350);
        stage.setTitle("Hello!");
        stage.setScene(scene);
    }

    private void loadDonors() {
        donorData = FXCollections.observableArrayList();
        String url = "jdbc:mysql://localhost:3306/blooddonationmanagement";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement stmt = connection.createStatement()) {
            String query = "SELECT name, BloodType, contact FROM donor ORDER BY BloodType";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                donorData.add(new DonorInfo(
                        rs.getString("name"),
                        rs.getString("BloodType"),
                        rs.getString("contact")
                ));
            }
            donorsTable.setItems(donorData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class DonorInfo {
        private final String name;
        private final String bloodType;
        private final String contact;

        DonorInfo(String name, String bloodType, String contact) {
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
