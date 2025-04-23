package com.example.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WelcomePage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 650, 350);
            stage.setTitle("Blood Finder Application");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading the FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
