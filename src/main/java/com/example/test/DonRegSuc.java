package com.example.test;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DonRegSuc {

    @FXML
    private TableColumn<DonorRegController.RecipientInfo, String> BloodCol;

    @FXML
    private TableColumn<DonorRegController.RecipientInfo, String> ContactCol;

    @FXML
    private TableColumn<DonorRegController.RecipientInfo, String> NameCol;

    @FXML
    private TableView<DonorRegController.RecipientInfo> Rtable;

    @FXML
    public void initialize() {
        // Initialize the columns with the correct property values
        NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        BloodCol.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        ContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    // Method to set the recipients data in the table
    public void setMatchingRecipients(List<DonorRegController.RecipientInfo> recipients) {
        Rtable.setItems(FXCollections.observableArrayList(recipients));
    }

    @FXML
    private void handleGoBack() {
        Stage stage = (Stage) Rtable.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}