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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;

import static com.example.iransandbox.SpringBootJavaFXApplication.*;

@Controller
public class CreateInvoiceController {

    @Autowired
    public InvoicesItemRepository invoiceItemRepository;
    @Autowired
    public InvoiceRepository invoiceRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public ArrayList<Invoice_item> items = new ArrayList<>();
    public Product currentProduct;


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
    private TableColumn<Product, String> productQuantityColumn;

    @FXML
    private TableColumn<Product, String> productPriceColumn;

    @FXML
    private TableColumn<Product, String> productDiscountColumn;

    @FXML
    private TableColumn<Product, String> productDiscountedPriceColumn;

    @FXML
    private TextField productQuantityField;



    @FXML
    private TableView<Invoice_item> invoiceItemsTable;

    @FXML
    private TableColumn<Invoice_item, String> productNameColumn1;

    @FXML
    private TableColumn<Invoice_item, String> productPriceColumn1;

    @FXML
    private TableColumn<Invoice_item, String> productQuantityColumn1;

    @FXML
    private TableColumn<Invoice_item, String> productDiscountColumn1;

    @FXML
    private TableColumn<Invoice_item, String> productsTotalPriceColumn;



    @FXML
    private TextField customerPhoneNumber;

    @FXML
    private TextField customerFirstName;

    @FXML
    private TextField customerLastName;

    @FXML
    private TextField customerEmail;

    @FXML
    private Button registerCustomerButton;

    @FXML
    public void handleCreateInvoiceItem(ActionEvent event) {
        String quantity = productQuantityField.getText();
        if (quantity == null || quantity.trim().isEmpty()) {
            AlertHandler.showAlert("Error", "Quantity is required", "Please enter a valid quantity");
            return;
        }
        int quantityInt = Integer.parseInt(quantity);
        if (quantityInt < 1) {
            AlertHandler.showAlert("Error", "Quantity must be a positive integer", "Please enter a valid quantity");
            return;
        }
        Inventory inventory = inventoryRepository.findByProductId(currentProduct.getProduct_id());
        if (inventory == null || inventory.getQuantity() < quantityInt) {
            AlertHandler.showAlert("Error", "Not enough stock", "Please choose a quantity less than or equal to the available stock");
            return;
        }
        if (quantityInt > 10){
            AlertHandler.showAlert("Warning", "Warning", "Quantity is more than 10, consider purchasing in bulk");
            return;
        }
        if (currentProduct == null) {
            AlertHandler.showAlert("Error", "Product is required", "Please select a product");
            return;
        }

        int index = -1;
        for (Invoice_item item : items) {
            if (item.getProduct().getProduct_id().equals(currentProduct.getProduct_id())) {
                index = items.indexOf(item);
            }
        }

        Invoice_item invoiceItem = new Invoice_item();

        invoiceItem.setProduct(currentProduct);
        Long productPrice = featureRepository.getPriceByProduct(currentProduct.getProduct_id());
        double discount = featureRepository.getDiscountByProduct(currentProduct.getProduct_id()) /100.0 ;
        int discountedPrice = (int) (productPrice * (1 - discount));
        invoiceItem.setUnit_price(discountedPrice);
        invoiceItem.setDiscount(discount);
        if (index == -1) {
            invoiceItem.setQuantity(quantityInt);
            invoiceItem.setFinal_total_price(quantityInt * discountedPrice);
            items.add(invoiceItem);
        }else{
            invoiceItem.setQuantity(items.get(index).getQuantity() + quantityInt);
            invoiceItem.setFinal_total_price((items.get(index).getQuantity() + quantityInt) * discountedPrice);
            items.set(index, invoiceItem);
        }
        inventory.setQuantity(inventory.getQuantity() - quantityInt);
        inventoryRepository.update(inventory);
        updateProductTable();
        updateInvoiceItems();
        productQuantityField.clear();
    }

    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ObservableList<Invoice_item> invoiceItems = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        items = new ArrayList<>();
        updateProductTable();
        updateInvoiceItems();
    }

    public void updateProductTable(){
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        productCategoryColumn.setCellValueFactory(cellData -> {
            Category category = cellData.getValue().getCategory();
            return category != null ? new SimpleStringProperty(category.getName()) : new SimpleStringProperty("No Category");
        });
        productQuantityColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            Inventory inventory = inventoryRepository.findByProductId(product.getProduct_id());
            return new SimpleStringProperty(inventory.getQuantity() + "");
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

        productPriceColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            String price = featureRepository.getPriceByProduct(product.getProduct_id()) + "";
            return new SimpleStringProperty(price);
        });

        productDiscountColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            String discount = featureRepository.getDiscountByProduct(product.getProduct_id()) + " %";
            return new SimpleStringProperty(discount);
        });

        productDiscountedPriceColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            long price = featureRepository.getPriceByProduct(product.getProduct_id());
            double discount = featureRepository.getDiscountByProduct(product.getProduct_id()) / 100.0;
            long discountedPrice = (long) (price - (price * discount));
            return new SimpleStringProperty(discountedPrice + "");
        });
        loadProducts();
    }

    public void updateInvoiceItems(){
        productNameColumn1.setCellValueFactory(cellDate -> {
            Invoice_item invoiceItem = cellDate.getValue();
            Product product = invoiceItem.getProduct();
            String productName = product.getName();
            return new SimpleStringProperty(productName);
        });
        productPriceColumn1.setCellValueFactory(cellData -> {
            Invoice_item invoiceItem = cellData.getValue();
            Product product = invoiceItem.getProduct();
            long price = featureRepository.getPriceByProduct(product.getProduct_id());
            return new SimpleStringProperty(String.valueOf(price));
        });
        productQuantityColumn1.setCellValueFactory(cellData -> {
            Invoice_item invoiceItem = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(invoiceItem.getQuantity()));
        });
        productDiscountColumn1.setCellValueFactory(cellData -> {
            Invoice_item invoiceItem = cellData.getValue();
            Product product = invoiceItem.getProduct();
            String discount = featureRepository.getDiscountByProduct(product.getProduct_id()) + " %";
            return new SimpleStringProperty(discount);
        });
        productsTotalPriceColumn.setCellValueFactory(cellData -> {
            Invoice_item invoiceItem = cellData.getValue();
            Product product = invoiceItem.getProduct();
            long price = featureRepository.getPriceByProduct(product.getProduct_id());
            double discountPrice = price * (1 - featureRepository.getDiscountByProduct(product.getProduct_id()) / 100.0);
            double totalPrice = invoiceItem.getQuantity() * discountPrice;
            return new SimpleStringProperty(String.valueOf(Math.round(totalPrice)));
        });

        loadInvoiceItems();
    }

    private void loadProducts() {
        products.setAll(productRepository.findAll());
        productTable.setItems(products);
    }

    @FXML
    private void handleCreateInvoice(ActionEvent event) {
        Invoice invoice = new Invoice();
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        invoice.setCreatedAt(currentDate);
        User user = globalUserRepository.findUserByUsername(currentUser);
        invoice.setUser(user);
        if (items.isEmpty()) {
            AlertHandler.showAlert("Error", "No items selected", "Please select at least one item");
            return;
        }
        String phoneNumber = customerPhoneNumber.getText();
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            AlertHandler.showAlert("Error", "Phone number is required", "Please enter a valid phone number");
            return;
        }
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
        System.out.println("customer == null : " + (customer == null));
        System.out.println("registerCustomerButton.isVisible() : " + registerCustomerButton.isVisible());
        System.out.println("registerCustomerButton.visibleProperty() : " + registerCustomerButton.visibleProperty());
        if (customer == null){
            customerFirstName.setVisible(true);
            customerLastName.setVisible(true);
            customerEmail.setVisible(true);
            registerCustomerButton.setVisible(true);
            if (registerCustomerButton.isVisible()){
                customer = new Customer();
                customer.setPhone_number(phoneNumber);
                String firstName = customerFirstName.getText();
                String lastName = customerLastName.getText();
                if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()){
                    AlertHandler.showAlert("Error", "First name and last name are required", "Please enter valid first name and last name");
                    return;
                }
                customer.setFirst_name(firstName);
                customer.setLast_name(lastName);
                customer.setEmail(customerEmail.getText());
                customerRepository.save(customer);
            }else{
                AlertHandler.showAlert("Error", "Customer not found", "Please register a new customer or enter a valid phone number");
                return;
            }
        }
        invoice.setCustomer(customer);
        invoiceRepository.save(invoice);
        int totalAmount = 0;
        for (Invoice_item item : items) {
            item.setInvoice(invoice);
            invoiceItemRepository.save(item);
            totalAmount += item.getFinal_total_price();
        }
        invoice.setTotal_amount(totalAmount);
        invoiceRepository.update(invoice);
        initialize();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            for (Invoice_item item : items) {
                Inventory inventory = inventoryRepository.findByProductId(item.getProduct().getProduct_id());
                inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
                inventoryRepository.update(inventory);
            }
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
    private void productSelection(MouseEvent event) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct!= null) {
            currentProduct = selectedProduct;
        }
    }

    public void loadInvoiceItems(){
        invoiceItems.setAll(items);
        invoiceItemsTable.setItems(invoiceItems);
    }

    @FXML
    private void handleRegisterCustomer(ActionEvent event) {
        String phoneNumber = customerPhoneNumber.getText();
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            AlertHandler.showAlert("Error", "Phone number is required", "Please enter a valid phone number");
            return;
        }
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
        if (customer == null){
            if (registerCustomerButton.isVisible()){
                customer = new Customer();
                customer.setPhone_number(phoneNumber);
                String firstName = customerFirstName.getText();
                String lastName = customerLastName.getText();
                if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()){
                    AlertHandler.showAlert("Error", "First name and last name are required", "Please enter valid first name and last name");
                    return;
                }
                customer.setFirst_name(firstName);
                customer.setLast_name(lastName);
                customer.setEmail(customerEmail.getText());
                customerRepository.save(customer);
            }else{
                customerFirstName.setVisible(true);
                customerLastName.setVisible(true);
                customerEmail.setVisible(true);
                registerCustomerButton.setVisible(true);
                AlertHandler.showAlert("Error", "Customer not found", "Please register a new customer or enter a valid phone number");
                return;
            }
        }
    }
}
