package com.example.controller;

import com.example.iransandbox.SpringBootJavaFXApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static com.example.iransandbox.SpringBootJavaFXApplication.applicationContext;


@Controller
public class MainController {

    @FXML
    public Button categoryManagementButton;
    @FXML
    public Button employeeManagementButton;
    @FXML
    public Button productManagementButton;


    @FXML
    public void initialize() {
        if (SpringBootJavaFXApplication.currentRole.equals("employee")){
            employeeManagementButton.setVisible(false);
            categoryManagementButton.setVisible(false);
            productManagementButton.setVisible(false);
        }
    }


    @FXML
    private void handleEmployeeManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/employee.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Employee Management");
            stage.setScene(new Scene(root));
            stage.show();

            //close previous stage
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load employee.fxml file: " + e.getLocalizedMessage());
        }
    }

    @FXML
    private void handleCategoryManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/category.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Category Management");
            stage.setScene(new Scene(root));
            stage.show();

            //close previous stage
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load category.fxml file: " + e.getLocalizedMessage());
        }
    }

    @FXML
    private void handleProductManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/product.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Product Management");
            stage.setScene(new Scene(root));
            stage.show();

            //close previous stage
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load product.fxml file: " + e.getCause());
        }
    }

    @FXML
    private void handleInventoryManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inventory.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inventory Management");
            stage.setScene(new Scene(root));
            stage.show();

            //close previous stage
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load inventory.fxml file: " + e.getLocalizedMessage());
        }
    }

    @FXML
    private void handleInvoiceManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/invoice.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Invoice Management");
            stage.setScene(new Scene(root));
            stage.show();

            //close previous stage
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load invoice.fxml file: " + e.getCause());
        }
    }


    @FXML
    private void handleInvoiceCreation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createInvoice.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Invoice Creation");
            stage.setScene(new Scene(root));
            stage.show();

            //close previous stage
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load createInvoice.fxml file: " + e.getCause());
        }
    }


    @FXML
    private void handleReportManagement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/report.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Report Management");
            stage.setScene(new Scene(root));
            stage.show();

            //close previous stage
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load report.fxml file: " + e.getCause());
        }
    }

    @FXML
    private void handleLogOut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

            //close previous stage
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            parentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load user.fxml file: " + e.getLocalizedMessage());
        }
    }

}
