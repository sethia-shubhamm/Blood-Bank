package com.example.test;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WelcomePageController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(WelcomePageController.class.getName());

    @FXML
    private Label Welcome;

    @FXML
    private Button donate;

    @FXML
    private Button receive;

    @FXML
    private Button viewDonors;

    @FXML
    private Button viewRecipients;

    @FXML
    private Button exit;

    @FXML
    private ImageView image;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set the image for the ImageView via FXML
        image.setImage(new Image("file:/C:/Users/shubh/Desktop/BloodFinder2/BloodFinder2/BloodFinder/src/main/resources/com/example/test/logo.PNG"));
        image.setVisible(true);
        logAvailableResources();
    }

    @FXML
    void donateWindow(MouseEvent event) throws IOException {
        loadWindow(donate, "/com/example/test/DonorRegistrationPage.fxml", "Donor Registration");
    }

    @FXML
    void receiveWindow(MouseEvent event) throws IOException {
        loadWindow(receive, "/com/example/test/ReceiverRegistration.fxml", "Receiver Registration");
    }

    @FXML
    void viewDonorsWindow(MouseEvent event) throws IOException {
        loadWindow(viewDonors, "/com/example/test/ViewDonorsPage.fxml", "View Donors");
    }

    @FXML
    void viewRecipientsWindow(MouseEvent event) throws IOException {
        loadWindow(viewRecipients, "/com/example/test/ViewRecipientsPage.fxml", "View Recipients");
    }

    @FXML
    void exitApplication(MouseEvent event) {
        Platform.exit();
    }

    private void loadWindow(Button button, String resourcePath, String title) throws IOException {
        URL resource = getClass().getResource(resourcePath);
        if (resource == null) {
            String errorMsg = "Resource not found: " + resourcePath;
            LOGGER.log(Level.SEVERE, errorMsg);
            throw new IOException(errorMsg);
        }
        Stage stage = (Stage) button.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle(title);
        stage.setScene(scene);
    }

    private void logAvailableResources() {
        try {
            Enumeration<URL> resources = getClass().getClassLoader().getResources("com/example/test/");
            while (resources.hasMoreElements()) {
                URL resourceUrl = resources.nextElement();
                LOGGER.log(Level.INFO, "Available resource: " + resourceUrl);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to list resources", e);
        }
    }
}