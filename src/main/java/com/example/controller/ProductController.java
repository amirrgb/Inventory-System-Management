package com.example.controller;

import com.example.model.Category;
import com.example.model.Feature;
import com.example.model.Feature_value;
import com.example.model.Product;
import com.example.repository.CategoryRepository;
import com.example.repository.FeatureRepository;
import com.example.repository.FeatureValueRepository;
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

    private static Feature_value currentFeatureValue;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FeatureValueRepository featureValueRepository;

    @Autowired
    private FeatureRepository featureRepository;


    @FXML
    private TextField productNameField;

    @FXML
    private TextField productDescriptionField;

    @FXML
    public TextField productCategoryIdField;

    @FXML
    private TextField searchCategoryField;

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
    private TextField searchProductField;

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
    private TextField featureValueField;

    @FXML
    private TableView<Feature_value> featureTable;

    @FXML
    private TableColumn<Feature_value, String> featureNameColumn;

    @FXML
    private TableColumn<Feature_value, String> featureTypeColumn;

    @FXML
    private TableColumn<Feature_value, String> featureValueColumn;


    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ObservableList<Feature_value> featureValues = FXCollections.observableArrayList();
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

        featureNameColumn.setCellValueFactory(cellData -> {
            String featureName = cellData.getValue().getFeature().getFeature();
            return featureName!= null? new SimpleStringProperty(featureName) : new SimpleStringProperty("No Feature");
        });
        featureTypeColumn.setCellValueFactory(cellData -> {
            String featureType = cellData.getValue().getFeature().getFeature_type();
            return featureType!= null? new SimpleStringProperty(featureType) : new SimpleStringProperty("No Type");
        });
        featureValueColumn.setCellValueFactory(cellData -> {
            String featureValue = cellData.getValue().getValue();
            return featureValue!= null? new SimpleStringProperty(featureValue) : new SimpleStringProperty("No Value");
        });

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
        List<Feature_value> productFeatureValues = featureValueRepository.findAllByProduct(currentProduct.getProduct_id());
        if (productFeatureValues == null) {
            return;
        }
        featureValues.setAll(productFeatureValues);
        featureTable.setItems(featureValues);
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
        createDefaultFeatureValues(product);
        loadFeatures();
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
        Feature_value selectedFeatureValue = featureTable.getSelectionModel().getSelectedItem();
        currentFeatureValue = selectedFeatureValue;
        featureNameField.setText(selectedFeatureValue.getFeature().getFeature());
        featureValueField.setText(selectedFeatureValue.getValue().trim());
    }

    @FXML
    private void handleEditFeature(ActionEvent event) {
        String featureName = featureNameField.getText();
        String featureValue = featureValueField.getText();

        if (!featureName.equals(currentFeatureValue.getFeature().getFeature())) {
            warningTextArea.setText("Feature Name cannot be changed");
            return;
        }

        if (featureValue == null || featureValue.trim().isEmpty()){
            warningTextArea.setText("Feature Value is required");
            return;
        }

        Feature_value selectedFeatureValue = currentFeatureValue;
        selectedFeatureValue.setValue(featureValue);
        featureValueRepository.update(selectedFeatureValue);
        loadFeatures();

        clearFeatureFields(null);
    }

    private void clearWarningText() {
        warningTextArea.clear();
    }

    @FXML
    public void searchForProduct(ActionEvent event) {
        String productName = searchProductField.getText().trim();
        if (productName.isEmpty()) {
            loadProducts();
        } else {
            products.setAll(productRepository.findByName(productName));
            productTable.setItems(products);
        }
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

    private void createDefaultFeatureValues(Product product) {
        Category category = product.getCategory();
        List<Feature> features = featureRepository.findAllByCategory(category.getCategory_id());
        System.out.println("--- --- --- features of category : " + category.getName() + " is " + features.size());
        for (Feature feature : features) {
            System.out.println("--- --- --- feature is : " + feature.getFeature());
            Feature_value featureValue = new Feature_value();
            featureValue.setFeature(feature);
            featureValue.setValue(" ");
            featureValue.setProduct(product);
            featureValueRepository.save(featureValue);
        }
    }
}
