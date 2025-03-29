package com.example.coursework.fxControllers;

import com.example.coursework.hibernateControllers.GenericHibernate;
import com.example.coursework.model.Admin;
import com.example.coursework.model.Client;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
        if(clientRadio.isSelected()){
            //TODO check if fields have valid values, use alerts
            Client client = new Client(nameField.getText(), surnameField.getText(), usernameField.getText(), passwordField.getText(), emailField.getText(), birthDateField.getValue(), addressField.getText());
            genericHibernate.create(client);
            //TODO inform about success/fail
        }else{
            //TODO check if fields have valid values, use alerts
            Admin admin = new Admin(nameField.getText(), surnameField.getText(), usernameField.getText(), passwordField.getText(), emailField.getText(), employmentDate.getValue(), phoneNumField.getText());
            genericHibernate.create(admin);
            //TODO inform about success/fail
        }
    }
}
