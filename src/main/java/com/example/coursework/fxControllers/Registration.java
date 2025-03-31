package com.example.coursework.fxControllers;

import com.example.coursework.hibernateControllers.GenericHibernate;
import com.example.coursework.model.Admin;
import com.example.coursework.model.Client;
import com.example.coursework.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;

public class Registration {

    @FXML
    public TextField usernameField;
    @FXML
    public TextField nameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField emailField;
    @FXML
    public DatePicker birthDateField;
    @FXML
    public TextField addressField;
    @FXML
    public RadioButton clientRadio;
    @FXML
    public RadioButton adminRadio;
    @FXML
    public DatePicker employmentDate;
    @FXML
    public AnchorPane adminFunctionalityArea;
    @FXML
    public TextField phoneNumField;

    EntityManagerFactory entityManagerFactory;
    GenericHibernate genericHibernate;
    private boolean isEditMode = false;
    private User userToEdit = null;

    //This method is called from login form, we do not open new connections each time the window is open
    //We have 2nd parameter that is boolean.This form is going to be loaded from Login window or for Admins in main form
    //When it is called from Login form, we will show only Client fields
    public void setData(EntityManagerFactory entityManagerFactory, boolean fromLogin) {
        this.entityManagerFactory = entityManagerFactory;
        genericHibernate = new GenericHibernate(entityManagerFactory);
        if (fromLogin) hideFields();
    }

    private void hideFields() {
        adminFunctionalityArea.setVisible(false);
    }

    public void createNewUser() {
        if (isEditMode && userToEdit != null) {
            // Update existing user
            updateExistingUser();
        } else {
            // Create new user
            createBrandNewUser();
        }
    }
    
    private void createBrandNewUser() {
        if (clientRadio.isSelected()) {
            if (validateClientFields()) {
                Client client = new Client(nameField.getText(), surnameField.getText(), usernameField.getText(), passwordField.getText(), emailField.getText(), birthDateField.getValue(), addressField.getText());
                try {
                    genericHibernate.create(client);
                    JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Success", "Client created successfully!");
                    clearFields();
                } catch (Exception e) {
                    JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Could not create client: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else if (adminRadio.isSelected()) {
            if (validateAdminFields()) {
                Admin admin = new Admin(nameField.getText(), surnameField.getText(), usernameField.getText(), passwordField.getText(), emailField.getText(), employmentDate.getValue(), phoneNumField.getText());
                try {
                    genericHibernate.create(admin);
                    JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Success", "Admin created successfully!");
                    clearFields();
                } catch (Exception e) {
                    JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Could not create admin: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, "Warning", "Please select Client or Admin.");
        }
    }
    
    private void updateExistingUser() {
        try {
            if (userToEdit instanceof Client client) {
                client.setName(nameField.getText());
                client.setSurname(surnameField.getText());
                client.setLogin(usernameField.getText());
                client.setEmail(emailField.getText());
                client.setBirthDate(birthDateField.getValue());
                client.setAddress(addressField.getText());
                
                // Update password only if a new one is provided
                if (!passwordField.getText().isEmpty()) {
                    client.setPassword(passwordField.getText());
                }
                
                genericHibernate.update(client);
                JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Success", "Client updated successfully!");
            } else if (userToEdit instanceof Admin admin) {
                admin.setName(nameField.getText());
                admin.setSurname(surnameField.getText());
                admin.setLogin(usernameField.getText());
                admin.setEmail(emailField.getText());
                admin.setEmploymentDate(employmentDate.getValue());
                admin.setPhoneNum(phoneNumField.getText());
                
                // Update password only if a new one is provided
                if (!passwordField.getText().isEmpty()) {
                    admin.setPassword(passwordField.getText());
                }
                
                genericHibernate.update(admin);
                JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Success", "Admin updated successfully!");
            }
            
            // Close the window after successful update
            javafx.stage.Stage stage = (javafx.stage.Stage) nameField.getScene().getWindow();
            stage.close();
            
        } catch (Exception e) {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "Could not update user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateClientFields() {
        if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || usernameField.getText().isEmpty()
                || passwordField.getText().isEmpty() || emailField.getText().isEmpty() || birthDateField.getValue() == null
                || addressField.getText().isEmpty()) {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, "Warning", "All client fields are required.");
            return false;
        }
        if (!isValidEmail(emailField.getText())) {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, "Warning", "Invalid email format.");
            return false;
        }
        if (birthDateField.getValue().isAfter(LocalDate.now())) {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, "Warning", "Birth date cannot be in the future.");
            return false;
        }
        // Add more validation if needed (e.g., password strength)
        if (passwordField.getText().isEmpty() || passwordField.getText().length() < 6) {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, "Warning", "Password must be at least 6 characters long.");
            return false;
        }
        if (usernameField.getText().isEmpty()) {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, "Warning", "Username cannot be empty.");
            return false;
        }
        if (genericHibernate.getAllRecords(User.class).stream().anyMatch(user -> user.getName().equals(nameField.getText()))) {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, "Warning", "Username already exists.");
            return false;
        }
        return true;
    }

    private boolean validateAdminFields() {
        if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || usernameField.getText().isEmpty()
                || passwordField.getText().isEmpty() || emailField.getText().isEmpty() || employmentDate.getValue() == null
                || phoneNumField.getText().isEmpty()) {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, "Warning", "All admin fields are required.");
            return false;
        }
        if (!isValidEmail(emailField.getText())) {
            JavaFxUtils.generateAlert(javafx.scene.control.Alert.AlertType.WARNING, "Warning", "Invalid email format.");
            return false;
        }
        // Add more validation if needed (e.g., phone number format)
        return true;
    }

    private boolean isValidEmail(String email) {
        // Basic email validation regex (not 100% foolproof, but good enough for most cases)
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void clearFields() {
        nameField.clear();
        surnameField.clear();
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        birthDateField.setValue(null);
        addressField.clear();
        employmentDate.setValue(null);
        phoneNumField.clear();
        clientRadio.setSelected(false);
        adminRadio.setSelected(false);
    }

    public void setDataForEdit(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        genericHibernate = new GenericHibernate(entityManagerFactory);
        this.isEditMode = true;
        this.userToEdit = user;
        
        // Populate fields with user data
        nameField.setText(user.getName());
        surnameField.setText(user.getSurname());
        usernameField.setText(user.getLogin());
        // Password field is left empty for security reasons
        emailField.setText(user.getEmail());
        
        if (user instanceof Client client) {
            clientRadio.setSelected(true);
            adminRadio.setSelected(false);
            birthDateField.setValue(client.getBirthDate());
            addressField.setText(client.getAddress());
            adminFunctionalityArea.setVisible(true);
        } else if (user instanceof Admin admin) {
            adminRadio.setSelected(true);
            clientRadio.setSelected(false);
            employmentDate.setValue(admin.getEmploymentDate());
            phoneNumField.setText(admin.getPhoneNum());
            adminFunctionalityArea.setVisible(true);
        }
    }
}