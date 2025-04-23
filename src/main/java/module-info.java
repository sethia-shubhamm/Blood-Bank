module com.example.test {
    requires javafx.controls;
    requires javafx.fxml;

    // Add any other required modules
    requires java.sql;
    requires java.desktop;

    // Opens your packages if necessary
    opens com.example.test to javafx.fxml;

    // Exports your packages
    exports com.example.test;
}