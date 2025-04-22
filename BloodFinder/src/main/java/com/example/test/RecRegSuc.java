package com.example.test;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class RecRegSuc {

    @FXML
    private TableView<ReceiveRegController.DonorInfo> donorTable;

    @FXML
    private TableColumn<ReceiveRegController.DonorInfo, String> nameColumn;

    @FXML
    private TableColumn<ReceiveRegController.DonorInfo, String> bloodTypeColumn;

    @FXML
    private TableColumn<ReceiveRegController.DonorInfo, String> contactColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bloodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    public void populateTable(ObservableList<ReceiveRegController.DonorInfo> donorData) {
        donorTable.setItems(donorData);
    }

    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) donorTable.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WelcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 350);
        stage.setTitle("Blood Finder Application");
        stage.setScene(scene);
    }
}