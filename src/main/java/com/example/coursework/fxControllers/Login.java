package com.example.coursework.fxControllers;

import com.example.coursework.HelloApplication;
import com.example.coursework.hibernateControllers.CustomHibernate;
import com.example.coursework.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    @FXML
    public PasswordField pswField;
    @FXML
    public TextField loginField;
    // Once the program starts, connect to database
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("book_exchange");
    private CustomHibernate customHibernate;

    @FXML
    public void initialize() {
        customHibernate = new CustomHibernate(entityManagerFactory);
    }

    public void validateAndLoad() throws IOException {
        // Validate input fields first
        if (loginField.getText().isEmpty() || pswField.getText().isEmpty()) {
            JavaFxUtils.generateAlert(Alert.AlertType.WARNING, "Input Error", "Username and password cannot be empty");
            return;
        }
        
        if (pswField.getText().length() < 6) {
            JavaFxUtils.generateAlert(Alert.AlertType.WARNING, "Input Error", "Password must be at least 6 characters long");
            return;
        }
        
        // Check if user with given credentials exists
        User user = customHibernate.getUserByCredentials(loginField.getText(), pswField.getText());
        if (user != null) {
            // If all is well then load main window
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-window.fxml"));
            Parent parent = fxmlLoader.load();
            MainWindow mainWindow = fxmlLoader.getController();
            // Pass the connection and user
            mainWindow.setData(entityManagerFactory, user);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) pswField.getScene().getWindow();
            stage.setTitle("Book exchange platform!");
            stage.setScene(scene);
            stage.show();
        } else {
            JavaFxUtils.generateAlert(Alert.AlertType.INFORMATION, "No such user", "Check login and password");
        }
    }

    public void newUserForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registration.fxml"));
        // I need to call load method, so I could access controller
        Parent parent = fxmlLoader.load();
        Registration registration = fxmlLoader.getController();
        registration.setData(entityManagerFactory, true);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("Book exchange platform!");
        stage.setScene(scene);
        // Modality controls how the newly created window should behave. In this case, the newly opened window must first be closed so that we could work with previous window
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}