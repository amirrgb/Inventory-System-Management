package com.example.controller;

import com.example.model.Category;
import com.example.model.Feature;
import com.example.model.Product;
import com.example.repository.CategoryRepository;
import com.example.repository.FeatureRepository;
import com.example.repository.ProductRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
public class ProductController {

    private static Product currentProduct;

    private static Feature currentFeature;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField productDescriptionField;

    @FXML
    public TextField productCategoryIdField;

    @FXML
    public TextField productCategoryNameField;

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, BigInteger> categoryIdColumn;

    @FXML
    private TableColumn<Category, String> categoryNameColumn;

    @FXML
    public TextArea warningTextArea;



    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, BigInteger> productIdColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, String> productDescriptionColumn;

    @FXML
    private TableColumn<Product, String> productCategoryColumn;



    @FXML
    private TextField featureNameField;

    @FXML
    private TextField featureTypeField;

    @FXML
    private TextField featureValueField;

    @FXML
    private TableView<Feature> featureTable;

    @FXML
    private TableColumn<Feature, BigInteger> featureIdColumn;

    @FXML
    private TableColumn<Feature, String> featureNameColumn;

    @FXML
    private TableColumn<Feature, String> featureTypeColumn;

    @FXML
    private TableColumn<Feature, String> featureValueColumn;


    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ObservableList<Feature> features = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        productCategoryColumn.setCellValueFactory(cellData -> {
            Category category = cellData.getValue().getCategory();
            return category != null ? new SimpleStringProperty(category.getName()) : new SimpleStringProperty("No Category");
        });

        productCategoryColumn.setCellFactory(column -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        loadProducts();

        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadCategories();

        featureIdColumn.setCellValueFactory(new PropertyValueFactory<>("feature_id"));
        featureNameColumn.setCellValueFactory(new PropertyValueFactory<>("feature"));
        featureTypeColumn.setCellValueFactory(new PropertyValueFactory<>("feature_type"));
        featureValueColumn.setCellValueFactory(new PropertyValueFactory<>("feature_value"));

        loadFeatures();
    }

    private void loadCategories() {
        categories.setAll(categoryRepository.findAll());
        categoryTable.setItems(categories);
    }

    private void loadProducts() {
        products.setAll(productRepository.findAll());
        productTable.setItems(products);
    }

    private void loadFeatures() {
        if (currentProduct == null) {
            return;
        }
        List<Feature> productFeatures = featureRepository.findAllByProduct(currentProduct.getProduct_id());
        if (productFeatures == null) {
            return;
        }
        features.setAll(productFeatures);
        featureTable.setItems(features);
    }

    @FXML
    private void handleAddProduct(ActionEvent event) {
        String productName = productNameField.getText();
        String productDescription = productDescriptionField.getText();
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();

        if (productName == null || productName.trim().isEmpty()){
            warningTextArea.setText("Product Name is required");
            return;
        }

        if (!Character.isLetter(productName.charAt(0))) {
            warningTextArea.setText("Product Name must start with a letter");
            return;
        }

        if (productDescription == null || productDescription.trim().isEmpty()){
            warningTextArea.setText("Product Description is required");
            return;
        }

        if (selectedCategory == null) {
            warningTextArea.setText("Please select a category");
            return;
        }

        if (productRepository.findByName(productName)!= null) {
            warningTextArea.setText("Product with the same name already exists");
            return;
        }

        Product product = new Product();
        product.setName(productName);
        product.setDescription(productDescription);
        product.setCategory(selectedCategory);

        productRepository.save(product);
        currentProduct = product;
        loadProducts();
        getFeaturesOfSameProducts();
        clearProductFields(null);
    }

    @FXML
    private void handleEditProduct(ActionEvent event) {
        String productName = productNameField.getText();
        String productDescription = productDescriptionField.getText();
        Category selectedCategory = categoryRepository.findById(BigInteger.valueOf(Long.parseLong(productCategoryIdField.getText())));

        if (productName == null || productName.trim().isEmpty()){
            warningTextArea.setText("Product Name is required");
            return;
        }

        if (productDescription == null || productDescription.trim().isEmpty()){
            warningTextArea.setText("Product Description is required");
            return;
        }

        if (selectedCategory == null) {
            warningTextArea.setText("Please select a category");
            return;
        }

        Product selectedProduct = currentProduct;
        selectedProduct.setName(productName);
        selectedProduct.setDescription(productDescription);
        selectedProduct.setCategory(selectedCategory);

        productRepository.update(selectedProduct);
        loadProducts();

        clearProductFields(null);
    }

    @FXML
    private void handleDeleteProduct(ActionEvent event) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        productRepository.delete(selectedProduct);
        loadProducts();
    }

    @FXML
    private void handleAddFeature(ActionEvent event) {
        String featureName = featureNameField.getText();
        String featureType = featureTypeField.getText();
        String featureValue = featureValueField.getText();

        if (featureName == null || featureName.trim().isEmpty()){
            warningTextArea.setText("Feature Name is required");
            return;
        }

        if (featureType == null || featureType.trim().isEmpty()){
            warningTextArea.setText("Feature Type is required");
            return;
        }

        if (featureValue == null || featureValue.trim().isEmpty()){
            warningTextArea.setText("Feature Value is required");
            return;
        }

        if (featureRepository.findByNameAndProduct(featureName, currentProduct.getProduct_id()) != null){
            warningTextArea.setText("Feature with the same name already exists for the selected product");
            return;
        }

        Product selectedProduct = currentProduct;

        if (selectedProduct == null) {
            warningTextArea.setText("Please select a product");
            return;
        }

        Feature feature = new Feature();
        feature.setFeature(featureName);
        feature.setFeature_type(featureType);
        feature.setFeature_value(featureValue);
        feature.setProduct(selectedProduct);

        featureRepository.save(feature);
        updateFeaturesOfProductInSameCategory();
        loadFeatures();
    }

    @FXML
    private void handleEditFeature(ActionEvent event) {
        String featureName = featureNameField.getText();
        String featureType = featureTypeField.getText();
        String featureValue = featureValueField.getText();

        if (featureName == null || featureName.trim().isEmpty()){
            warningTextArea.setText("Feature Name is required");
            return;
        }

        if (featureType == null || featureType.trim().isEmpty()){
            warningTextArea.setText("Feature Type is required");
            return;
        }

        if (featureValue == null || featureValue.trim().isEmpty()){
            warningTextArea.setText("Feature Value is required");
            return;
        }

        Feature selectedFeature = currentFeature;
        selectedFeature.setFeature(featureName);
        selectedFeature.setFeature_type(featureType);
        selectedFeature.setFeature_value(featureValue);

        featureRepository.update(selectedFeature);
        loadFeatures();
    }

    @FXML
    private void handleDeleteFeature(ActionEvent event) {
        Feature selectedFeature = featureTable.getSelectionModel().getSelectedItem();
        featureRepository.delete(selectedFeature);
        loadFeatures();
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
    private void clearProductFields(ActionEvent event) {
        clearWarningText();
        productNameField.clear();
        productDescriptionField.clear();
        productCategoryIdField.clear();
        productCategoryNameField.clear();
    }

    @FXML
    private void clearFeatureFields(ActionEvent event) {
        clearWarningText();
        featureNameField.clear();
        featureTypeField.clear();
        featureValueField.clear();
    }

    @FXML
    private void setCategoryField(MouseEvent event){
        clearWarningText();
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        productCategoryIdField.setText(selectedCategory.getCategory_id().toString());
        productCategoryNameField.setText(selectedCategory.getName());
    }

    @FXML
    private void setProductFields(MouseEvent event) {
        clearWarningText();
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        currentProduct = selectedProduct;
        productNameField.setText(selectedProduct.getName());
        productDescriptionField.setText(selectedProduct.getDescription());
        productCategoryIdField.setText(selectedProduct.getCategory().getCategory_id().toString());
        productCategoryNameField.setText(selectedProduct.getCategory().getName());
        loadFeatures();
    }

    @FXML
    private void setFeatureFields(MouseEvent event) {
        clearWarningText();
        Feature selectedFeature = featureTable.getSelectionModel().getSelectedItem();
        currentFeature = selectedFeature;
        featureNameField.setText(selectedFeature.getFeature());
        featureTypeField.setText(selectedFeature.getFeature_type());
        featureValueField.setText(selectedFeature.getFeature_value());
    }

    private void clearWarningText() {
        warningTextArea.clear();
    }

    private void getFeaturesOfSameProducts() {
        Feature feature = new Feature();

        if(featureRepository.findByNameAndProduct("price",currentProduct.getProduct_id()) == null){
            feature.setFeature("price");
            feature.setFeature_type("int");
            feature.setFeature_value("0");
            feature.setProduct(currentProduct);
            featureRepository.save(feature);
        }

        if(featureRepository.findByNameAndProduct("discount",currentProduct.getProduct_id()) == null) {
            feature = new Feature();
            feature.setFeature("discount");
            feature.setFeature_type("int");
            feature.setFeature_value("0");
            feature.setProduct(currentProduct);
            featureRepository.save(feature);
        }

        Category category = currentProduct.getCategory();
        List<Product> sameProducts = productRepository.findAllByCategory(category.getCategory_id());
        for (Product product : sameProducts) {
            List<Feature> otherFeatures = featureRepository.findAllByProduct(product.getProduct_id());
            for (Feature otherFeature : otherFeatures) {
                String featureName = otherFeature.getFeature();
                if(featureRepository.findByNameAndProduct(featureName,currentProduct.getProduct_id()) == null) {
                    feature.setFeature(otherFeature.getFeature());
                    feature.setFeature_type(otherFeature.getFeature_type());
                    feature.setFeature_value(null);
                    feature.setProduct(currentProduct);
                    featureRepository.save(feature);
                }
            }
        }

        loadFeatures();
    }

    private void updateFeaturesOfProductInSameCategory() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            Feature feature = new Feature();

            if(featureRepository.findByNameAndProduct("price",product.getProduct_id()) == null){
                feature.setFeature("price");
                feature.setFeature_type("int");
                feature.setFeature_value("0");
                feature.setProduct(product);
                featureRepository.save(feature);
            }

            if(featureRepository.findByNameAndProduct("discount",product.getProduct_id()) == null) {
                feature = new Feature();
                feature.setFeature("discount");
                feature.setFeature_type("int");
                feature.setFeature_value("0");
                feature.setProduct(product);
                featureRepository.save(feature);
            }

            Category category = product.getCategory();
            List<Product> sameProducts = productRepository.findAllByCategory(category.getCategory_id());
            for (Product sameProduct : sameProducts) {
                List<Feature> otherFeatures = featureRepository.findAllByProduct(sameProduct.getProduct_id());
                for (Feature otherFeature : otherFeatures) {
                    String featureName = otherFeature.getFeature();
                    if(featureRepository.findByNameAndProduct(featureName,product.getProduct_id()) == null) {
                        feature.setFeature(otherFeature.getFeature());
                        feature.setFeature_type(otherFeature.getFeature_type());
                        feature.setFeature_value(null);
                        feature.setProduct(product);
                        featureRepository.save(feature);
                    }
                }
            }
        }

    }

}
