package com.example.controller;

import com.example.AlertHandler;
import com.example.model.*;
import com.example.repository.*;
import javafx.beans.property.SimpleStringProperty;
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
public class InventoryController {

    public static Category currentCategory;
    public static Product currentProduct;

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, BigInteger> categoryIdColumn;

    @FXML
    private TableColumn<Category, String> categoryNameColumn;

    @FXML
    private TableColumn<Category, String> categoryQuantity;

    @FXML
    private TableColumn<Category, String> categoryProductsQuantity;

    @FXML
    private TextField searchCategoryField;

    @FXML
    private TextField searchProductField;

    @FXML
    public TextField productName;

    @FXML
    private TextField productQuantity;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, BigInteger> productIdColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, String> productDescriptionColumn;

    @FXML
    private TableColumn<Product, String> productQuantityColumn;

    @FXML
    private TableColumn<Product, String> productPriceColumn;

    @FXML
    private TableColumn<Product, String> productsTotalPriceColumn;

    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    @Autowired
    private FeatureValueRepository featureValueRepository;

    @FXML
    public void initialize() {
        initializeQuantityForAllProducts();
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        updateQuantities();
    }

    public void initializeQuantityForAllProducts() {
        for (Category category : categoryRepository.findAll()) {
            List<Product> categoryProducts = productRepository.findAllByCategory(category.getCategory_id());
            for (Product product : categoryProducts) {
                Inventory inventory = inventoryRepository.findByProductId(product.getProduct_id());
                if (inventory == null) {
                    Inventory newInventory = new Inventory();
                    newInventory.setProduct(product);
                    newInventory.setQuantity(0);
                    inventoryRepository.saveOrUpdate(newInventory);
                }
            }
        }
    }


    public void updateQuantities() {
        categoryQuantity.setCellValueFactory(cellData -> {
            Category category = cellData.getValue();
            long productCount = productRepository.findAllByCategory(category.getCategory_id()).size();
            SimpleStringProperty productCountProperty = new SimpleStringProperty(String.valueOf(productCount));
            System.out.println(productCountProperty);
            return productCountProperty;
        });
        categoryProductsQuantity.setCellValueFactory(cellData -> {
            Category category = cellData.getValue();
            List<Product> categoryProducts = productRepository.findAllByCategory(category.getCategory_id());
            int totalQuantity = 0;
            for (Product product : categoryProducts) {
                Inventory inventory = inventoryRepository.findByProductId(product.getProduct_id());
                if (inventory!= null) {
                    totalQuantity += inventory.getQuantity();
                }
            }
            return new SimpleStringProperty(String.valueOf(totalQuantity));
        });
        productQuantityColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            Inventory inventory = inventoryRepository.findByProductId(product.getProduct_id());
            if (inventory!= null) {
                return new SimpleStringProperty(String.valueOf(inventory.getQuantity()));
            }
            return new SimpleStringProperty("0");
        });

        productPriceColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            Feature feature = featureRepository.findByNameAndCategory("price", product.getCategory().getCategory_id());
            Feature_value featureValue = featureValueRepository.findByProductAndFeature(product.getProduct_id(), feature.getFeature_id());
            return new SimpleStringProperty(featureValue.getValue());
        });

        productsTotalPriceColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            String currentPrice = productPriceColumn.getCellData(product);
            String currentQuantity = productQuantityColumn.getCellData(product);
            if (currentPrice == null || currentQuantity == null ||
                    currentPrice.equals("N/A") || currentQuantity.equals("0")) {
                return new SimpleStringProperty("N/A");
            }else{
                double price = Double.parseDouble(currentPrice);
                int quantity = Integer.parseInt(currentQuantity);
                return new SimpleStringProperty(String.format("%.3f", price * quantity));
            }
        });

        loadCategories();
        loadProducts();
        clearFields();
    }


    private void loadCategories() {
        categories.setAll(categoryRepository.findAll());
        categoryTable.setItems(categories);
    }

    private void loadProducts() {
        if (currentCategory ==null){
            return;
        }
        List<Product> categoryProducts = productRepository.findAllByCategory(currentCategory.getCategory_id());
        if (categoryProducts == null) {
            return;
        }
        products.setAll(categoryProducts);
        productTable.setItems(products);

    }

    @FXML
    private void categorySelection(MouseEvent event) {
        currentCategory = categoryTable.getSelectionModel().getSelectedItem();
        loadProducts();
    }

    @FXML
    private void productSelection(MouseEvent event) {
        currentProduct = productTable.getSelectionModel().getSelectedItem();
        productName.setText(currentProduct.getName());
        Inventory inventory = inventoryRepository.findByProductId(currentProduct.getProduct_id());
        if (inventory!= null) {
            productQuantity.setText(String.valueOf(inventory.getQuantity()));
        } else {
            productQuantity.setText("0");
        }
//        loadFeatures();
    }

//    @FXML
//    private void handleWithdrawProductFromInventory(ActionEvent event) {
//        Inventory inventory = inventoryRepository.findByProductId(currentProduct.getProduct_id());
//        int currentQuantity = inventory.getQuantity();
//        int newQuantity = currentQuantity - Integer.parseInt(productQuantity.getText());
//        if (newQuantity < 0) {
//            AlertHandler.showAlert("Error", "Invalid Quantity", "quantity must be a positive integer.");
//        }else{
//            Transaction transaction = Transaction.makeTransaction(currentProduct, Integer.
//                    parseInt(productQuantity.getText()), "withdraw");
//            transactionRepository.save(transaction);
//            inventory.setQuantity(newQuantity);
//            inventoryRepository.saveOrUpdate(inventory);
//            updateQuantities();
//        }
//    }

    @FXML
    private void handleDepositProductToInventory(ActionEvent event) {
        Inventory inventory = inventoryRepository.findByProductId(currentProduct.getProduct_id());
        int currentQuantity = inventory.getQuantity();
        int newQuantity = currentQuantity + Integer.parseInt(productQuantity.getText());
        if (newQuantity < 0) {
            AlertHandler.showAlert("Error", "Invalid Quantity", "quantity must be a positive integer.");
        }else{
            Transaction transaction = Transaction.makeTransaction(currentProduct, Integer.
                    parseInt(productQuantity.getText()), "deposit");
            transactionRepository.save(transaction);
            inventory.setQuantity(newQuantity);
            inventoryRepository.saveOrUpdate(inventory);
            updateQuantities();
        }
    }

//    @FXML
//    private void handleSetProductQuantityIntoInventory(ActionEvent event) {
//        Inventory inventory = inventoryRepository.findByProductId(currentProduct.getProduct_id());
//        int newQuantity = Integer.parseInt(productQuantity.getText());
//        if (newQuantity < 0) {
//            AlertHandler.showAlert("Error", "Invalid Quantity", "quantity must be a positive integer.");
//        }else{
//            Transaction transaction = Transaction.makeTransaction(currentProduct, Integer.
//                    parseInt(productQuantity.getText()), "set");
//            transactionRepository.save(transaction);
//            inventory.setQuantity(newQuantity);
//            inventoryRepository.saveOrUpdate(inventory);
//            updateQuantities();
//        }
//    }


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

    public void clearFields() {
        productName.clear();
        productQuantity.clear();
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
    public void searchForProduct(ActionEvent event) {
        String productName = searchProductField.getText().trim();
        if (productName.isEmpty()) {
            loadProducts();
        } else {
            products.setAll(productRepository.findByName(productName));
            productTable.setItems(products);
        }
    }
}
