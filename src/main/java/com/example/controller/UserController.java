package com.example.controller;

import com.example.AlertHandler;
import com.example.iransandbox.SpringBootJavaFXApplication;
import com.example.model.User;
import com.example.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static com.example.iransandbox.SpringBootJavaFXApplication.applicationContext;

@Controller
public class UserController {

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
    private Button loginButton;

    @FXML
    private Button registerButton;


    @FXML
    public void initialize() {
        if (userRepository.isFirstTime()){
            loginButton.setVisible(false);
            registerButton.setVisible(true);
        }else{
            loginButton.setVisible(true);
            registerButton.setVisible(false);
            firstNameTextField.setVisible(false);
            lastNameTextField.setVisible(false);
        }
    }


    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (username == null || username.trim().isEmpty()){
            AlertHandler.showAlert("error","Username is required","Please enter a username.");
            return;
        }

        if (password == null || password.trim().isEmpty()){
            AlertHandler.showAlert("error","Password is required","Please enter a password.");
            return;
        }

        User user = userRepository.findUserByUsername(username);

        if (user == null){
            AlertHandler.showAlert("error","Invalid username","Please enter a valid username.");
            return;
        }

        if (!user.getPassword().equals(password)){
            AlertHandler.showAlert("error","Invalid password","Please enter a valid password.");
            return;
        }

        SpringBootJavaFXApplication.currentUser = username;
        SpringBootJavaFXApplication.currentRole = user.getRole();
        SpringBootJavaFXApplication.globalUserRepository = userRepository;
        System.out.println("Logged in : " + username + " as " + user.getRole());
        handleBack(event);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()){
            AlertHandler.showAlert("Error", "All fields must be filled", "Please fill all fields");
            return;
        }


        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirst_name(firstName);
        user.setLast_name(lastName);
        user.setRole("manager");
        userRepository.saveUser(user);
        SpringBootJavaFXApplication.currentUser = username;
        SpringBootJavaFXApplication.currentRole = "manager";
        SpringBootJavaFXApplication.globalUserRepository = userRepository;
        handleBack(event);
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
            e.printStackTrace();
            System.out.println("Failed to load menu.fxml file: " + e.getLocalizedMessage() + e.getCause() + e.getMessage());
        }
    }

}
