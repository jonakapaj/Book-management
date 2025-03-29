package com.example.coursework.fxControllers;

import javafx.scene.control.Alert;

public class JavaFxUtils {
    public static void generateAlert(Alert.AlertType alertType, String header, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Book Exchange Alert");
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
