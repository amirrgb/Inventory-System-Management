package com.example.controller;

import com.example.AlertHandler;
import com.example.model.User;
import com.example.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static com.example.iransandbox.SpringBootJavaFXApplication.applicationContext;

@Controller
public class EmployeeController {

    @Autowired
    private UserRepository userRepository;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField roleTextField;

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String role = roleTextField.getText();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || role.isEmpty()){
            AlertHandler.showAlert("Error", "All fields must be filled", "Please fill all fields");
            return;
        }

        if (userRepository.findUserByUsername(username)!= null){
            AlertHandler.showAlert("Error", "Username already exists", "Please choose a different username");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirst_name(firstName);
        user.setLast_name(lastName);
        user.setRole(role);
        userRepository.saveUser(user);
        AlertHandler.showAlert("Success", "Registration successful", "You can now log in with your new credentials");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene(root));
            stage.show();

            //close previous stage
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.hide();

        } catch (IOException e) {
            System.out.println("Failed to load menu.fxml file: " + e.getLocalizedMessage());
        }
    }

}
