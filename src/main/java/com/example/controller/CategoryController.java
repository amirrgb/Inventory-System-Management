package com.example.controller;

import com.example.AlertHandler;
import com.example.model.Category;
import com.example.model.Feature;
import com.example.repository.CategoryRepository;
import com.example.repository.FeatureRepository;
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
import java.util.List;

import static com.example.iransandbox.SpringBootJavaFXApplication.applicationContext;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FeatureRepository featureRepository;

    public Category currentCategory;
    public Feature currentFeature;

    @FXML
    private TextField categoryNameField;

    @FXML
    private TextField searchCategoryField;

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, BigInteger> categoryIdColumn;

    @FXML
    private TableColumn<Category, String> categoryNameColumn;



    @FXML
    private TextField featureNameField;

    @FXML
    private TextField featureTypeField;


    @FXML
    private TableView<Feature> featureTable;

    @FXML
    private TableColumn<Feature, BigInteger> featureIdColumn;

    @FXML
    private TableColumn<Feature, String> featureNameColumn;

    @FXML
    private TableColumn<Feature, String> featureTypeColumn;



    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<Feature> features = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadCategories();

        featureIdColumn.setCellValueFactory(new PropertyValueFactory<>("feature_id"));
        featureNameColumn.setCellValueFactory(new PropertyValueFactory<>("feature"));
        featureTypeColumn.setCellValueFactory(new PropertyValueFactory<>("feature_type"));

        loadFeatures();
    }

    private void loadFeatures() {
        if (currentCategory == null) {
            return;
        }
        List<Feature> categoryFeatures = featureRepository.findAllByCategory(currentCategory.getCategory_id());
        if (categoryFeatures == null) {
            return;
        }
        features.setAll(categoryFeatures);
        featureTable.setItems(features);
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
                category = categoryRepository.findByName(categoryName);
                Feature feature = new Feature();
                feature.setFeature_type("int");
                feature.setFeature("discount");
                feature.setCategory(category);
                featureRepository.save(feature);
                feature = new Feature();
                feature.setFeature_type("int");
                feature.setFeature("price");
                feature.setCategory(category);
                featureRepository.save(feature);
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

    private void loadCategories() {
        categories.setAll(categoryRepository.findAll());
        categoryTable.setItems(categories);
    }

    @FXML
    public void searchForCategory(ActionEvent event) {
        String categoryName = searchCategoryField.getText().trim();
        if (categoryName.isEmpty()) {
            loadCategories();
        } else {
            categories.setAll(categoryRepository.findByName(categoryName));
            categoryTable.setItems(categories);
        }
    }

    @FXML
    public void categorySelection(MouseEvent event) {
        currentCategory = categoryTable.getSelectionModel().getSelectedItem();
        categoryNameField.setText(currentCategory.getName());
        loadFeatures();
    }

    @FXML
    private void setFeatureFields(MouseEvent event) {
        Feature selectedFeature = featureTable.getSelectionModel().getSelectedItem();
        currentFeature = selectedFeature;
        featureNameField.setText(selectedFeature.getFeature());
        featureTypeField.setText(selectedFeature.getFeature_type());
    }

    @FXML
    private void clearFeatureFields(ActionEvent event) {
        featureNameField.clear();
        featureTypeField.clear();
        currentFeature = null;
    }

    @FXML
    private void handleAddFeature(ActionEvent event){
        String featureName = featureNameField.getText();
        String featureType = featureTypeField.getText();
        if (currentCategory == null) {
            AlertHandler.showAlert("Error", "Please select a category", "Please select a category before adding a feature.");
            return;
        }
        if (featureName!= null &&!featureName.trim().isEmpty() && featureType!= null &&!featureType.trim().isEmpty()) {
            if (featureRepository.findByNameAndCategory(featureName, currentCategory.getCategory_id()) != null) {
                AlertHandler.showAlert("Error", "Feature already exists", "Please enter a unique feature name for this category.");
                return;
            }
            Feature feature = new Feature();
            feature.setFeature(featureName);
            feature.setFeature_type(featureType);

            feature.setCategory(currentCategory);
            featureRepository.save(feature);
            loadFeatures();
            featureNameField.clear();
            featureTypeField.clear();
        } else{
            AlertHandler.showAlert("Error", "Feature name and type cannot be empty", "Please enter a valid feature name and type.");
        }
    }

    @FXML
    private void handleEditFeature(ActionEvent event) {
        Feature selectedFeature = featureTable.getSelectionModel().getSelectedItem();
        if (selectedFeature!= null) {
            String newFeatureName = featureNameField.getText();
            String newFeatureType = featureTypeField.getText();
            if (newFeatureName!= null &&!newFeatureName.trim().isEmpty() && newFeatureType!= null &&!newFeatureType.trim().isEmpty()) {
                selectedFeature.setFeature(newFeatureName);
                selectedFeature.setFeature_type(newFeatureType);
                featureRepository.update(selectedFeature);
                loadFeatures();
                featureNameField.clear();
                featureTypeField.clear();
            }else {
                AlertHandler.showAlert("Error", "Feature name and type cannot be empty", "Please enter a valid feature name and type.");
            }
        }else{
            AlertHandler.showAlert("Error", "Please select a feature", "Please select a feature before editing.");
        }
    }

    @FXML
    private void handleDeleteFeature(ActionEvent event) {
        Feature selectedFeature = featureTable.getSelectionModel().getSelectedItem();
        if (selectedFeature!= null) {
            featureRepository.delete(selectedFeature);
            loadFeatures();
            featureNameField.clear();
            featureTypeField.clear();
        } else {
            AlertHandler.showAlert("Error", "Please select a feature", "Please select a feature before deleting.");
        }
    }
}