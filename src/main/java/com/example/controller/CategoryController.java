package com.example.controller;

import com.example.AlertHandler;
import com.example.model.Category;
import com.example.repository.CategoryRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigInteger;

import static com.example.iransandbox.SpringBootJavaFXApplication.applicationContext;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @FXML
    private TextField categoryNameField;

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, BigInteger> categoryIdColumn;

    @FXML
    private TableColumn<Category, String> categoryNameColumn;

    private final ObservableList<Category> categories = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadCategories();
    }

    @FXML
    private void handleAddCategory(ActionEvent event) {
        String categoryName = categoryNameField.getText();
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            Category category = categoryRepository.findByName(categoryName);
            if (category == null) {
                category = new Category();
                category.setName(categoryName);
                categoryRepository.save(category);
                loadCategories();
                categoryNameField.clear();
            }else{
                AlertHandler.showAlert("Error", "Category already exists", "Please enter a unique category name.");
            }
        }else{
            AlertHandler.showAlert("Error", "Category name cannot be empty", "Please enter a valid category name.");
        }
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

    @FXML
    private void handleEditCategory(ActionEvent event) {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            String newCategoryName = categoryNameField.getText();
            if (newCategoryName != null && !newCategoryName.trim().isEmpty()) {
                selectedCategory.setName(newCategoryName);
                categoryRepository.updateCategory(selectedCategory);
                loadCategories();
                categoryNameField.clear();
            }
        }
    }

    @FXML
    private void setField(MouseEvent event){
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        categoryNameField.setText(selectedCategory.getName());
    }

    private void loadCategories() {
        categories.setAll(categoryRepository.findAll());
        categoryTable.setItems(categories);
    }

}