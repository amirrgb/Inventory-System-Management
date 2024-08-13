package com.example.controller;

import com.example.model.Customer;
import com.example.model.Invoice;
import com.example.model.Invoice_item;
import com.example.model.Product;
import com.example.repository.FeatureRepository;
import com.example.repository.InvoiceRepository;
import com.example.repository.InvoicesItemRepository;
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
public class InvoiceController {

    public static Invoice currentInvoice;

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private InvoicesItemRepository invoicesItemRepository;

    @FXML
    private TableView<Invoice> invoiceTable;

    @FXML
    private TableColumn<Invoice, BigInteger> invoiceIdColumn;

    @FXML
    private TableColumn<Invoice, String> date;

    @FXML
    private TableColumn<Invoice, String> customerInfo;

    @FXML
    private TableColumn<Invoice, String> employeeInfo;

    @FXML
    private TableColumn<Invoice, String> totalPrice;

    @FXML
    private TableView<Invoice_item> invoiceItemsTable;

    @FXML
    private TableColumn<Invoice_item, String> productNameColumn;

    @FXML
    private TableColumn<Invoice_item, String> productPriceColumn;

    @FXML
    private TableColumn<Invoice_item, String> productQuantityColumn;

    @FXML
    private TableColumn<Invoice_item, String> productDiscountColumn;

    @FXML
    private TableColumn<Invoice_item, String> productsTotalPriceColumn;

    private final ObservableList<Invoice> invoices = FXCollections.observableArrayList();
    private final ObservableList<Invoice_item> invoiceItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        updateInvoices();
        updateInvoiceItems();
    }

    public void updateInvoices(){

        invoiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("invoice_id"));
        date.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            return new SimpleStringProperty(invoice.getCreatedAt().toString());
        });
        customerInfo.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            Customer customer = invoice.getCustomer();
            String customerInfo = customer.getFirst_name() + " " + customer.getLast_name() + " : " + customer.getPhone_number();
            return new SimpleStringProperty(customerInfo);
        });
        employeeInfo.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            System.out.println("first name is : " + invoice.getUser().getFirst_name());
            System.out.println("last name is : " + invoice.getUser().getLast_name());
            String employeeInfo = invoice.getUser().getFirst_name() + " " + invoice.getUser().getLast_name();
            System.out.println("employee info is : " + employeeInfo);
            return new SimpleStringProperty(employeeInfo);
        });
        totalPrice.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(invoice.getTotal_amount()));
        });

        loadInvoices();
    }

    private void loadInvoices() {
        invoices.setAll(invoiceRepository.findAll());
        invoiceTable.setItems(invoices);
    }

    public void updateInvoiceItems(){
        productNameColumn.setCellValueFactory(cellDate -> {
            Invoice_item invoiceItem = cellDate.getValue();
            Product product = invoiceItem.getProduct();
            String productName = product.getName();
            return new SimpleStringProperty(productName);
        });
        productPriceColumn.setCellValueFactory(cellData -> {
            Invoice_item invoiceItem = cellData.getValue();
            Product product = invoiceItem.getProduct();
            long price = featureRepository.getPriceByProduct(product.getProduct_id());
            return new SimpleStringProperty(String.valueOf(price));
        });
        productQuantityColumn.setCellValueFactory(cellData -> {
            Invoice_item invoiceItem = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(invoiceItem.getQuantity()));
        });
        productDiscountColumn.setCellValueFactory(cellData -> {
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
            // removing decimal places
            return new SimpleStringProperty(String.valueOf(Math.round(totalPrice)));
        });

        loadInvoiceItems();
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
    private void invoiceSelection(MouseEvent event) {
        Invoice invoice = invoiceTable.getSelectionModel().getSelectedItem();
        if(invoice!= null){
            currentInvoice = invoice;
        }
        System.out.println("Invoice selected: " + currentInvoice.getInvoice_id());
        updateInvoiceItems();
        loadInvoiceItems();
    }

    public void loadInvoiceItems(){
        if (currentInvoice == null){
            return;
        }
        List<Invoice_item> invoiceItemsList = invoicesItemRepository.findAllByInvoiceId(currentInvoice.getInvoice_id());
        System.out.println("size of invoice items is : " + invoiceItemsList.size() + " for invoice id :" + currentInvoice.getInvoice_id()  + " " + currentInvoice.getCreatedAt());
        invoiceItems.setAll(invoiceItemsList);
        invoiceItemsTable.setItems(invoiceItems);
    }
}
