package com.example.controller;

import com.example.model.*;
import com.example.repository.FeatureRepository;
import com.example.repository.FeatureValueRepository;
import com.example.repository.InvoiceRepository;
import com.example.repository.InvoicesItemRepository;
import com.ghasemkiani.util.icu.PersianCalendar;
import com.ghasemkiani.util.icu.PersianDateFormat;
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.iransandbox.SpringBootJavaFXApplication.applicationContext;

;

@Controller
public class InvoiceController {

    public static Invoice currentInvoice;

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private InvoicesItemRepository invoicesItemRepository;
    @Autowired
    private FeatureValueRepository featureValueRepository;

    @FXML
    private TextField searchDateField;

    @FXML
    private TextField searchCustomerNameField;

    @FXML
    private TextField searchInvoiceIdField;

    @FXML
    private TableView<Invoice> invoiceTable;

    @FXML
    private TableColumn<Invoice, BigInteger> invoiceIdColumn;

    @FXML
    private TableColumn<Invoice, String> date;

    @FXML
    private TableColumn<Invoice, String> customerName;

    @FXML
    private TableColumn<Invoice, String> customerEmail;

    @FXML
    private TableColumn<Invoice, String> customerAddress;

    @FXML
    private TableColumn<Invoice, String> customerNationalId;

    @FXML
    private TableColumn<Invoice, String> customerCity;

    @FXML
    private TableColumn<Invoice, String> customerState;

    @FXML
    private TableColumn<Invoice, String> customerPhone;

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
            Timestamp currentDate = invoice.getCreatedAt();
            PersianCalendar persianCalendar = new PersianCalendar();
            persianCalendar.setTime(currentDate);
            PersianDateFormat persianDateFormat = new PersianDateFormat("yyyy/MM/dd");
            return new SimpleStringProperty(persianDateFormat.format(persianCalendar));
        });
        customerName.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            Customer customer = invoice.getCustomer();
            String customerInfo = customer.getFirst_name() + " " + customer.getLast_name();
            return new SimpleStringProperty(customerInfo);
        });

        customerEmail.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            Customer customer = invoice.getCustomer();
            String customerInfo = customer.getEmail();
            return new SimpleStringProperty(customerInfo);
        });

        customerAddress.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            Customer customer = invoice.getCustomer();
            String customerInfo = customer.getAddress();
            return new SimpleStringProperty(customerInfo);
        });

        customerNationalId.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            Customer customer = invoice.getCustomer();
            String customerInfo = customer.getNational_id();
            return new SimpleStringProperty(customerInfo);
        });

        customerCity.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            Customer customer = invoice.getCustomer();
            String customerInfo = customer.getCity();
            return new SimpleStringProperty(customerInfo);
        });

        customerState.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            Customer customer = invoice.getCustomer();
            String customerInfo = customer.getState();
            return new SimpleStringProperty(customerInfo);
        });

        customerPhone.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            Customer customer = invoice.getCustomer();
            String customerInfo = customer.getPhone_number();
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
            return new SimpleStringProperty(invoiceItem.getUnit_price() + "");
        });
        productQuantityColumn.setCellValueFactory(cellData -> {
            Invoice_item invoiceItem = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(invoiceItem.getQuantity()));
        });
        productDiscountColumn.setCellValueFactory(cellData -> {
            Invoice_item invoiceItem = cellData.getValue();
            String discount = invoiceItem.getDiscount() + " %";
            return new SimpleStringProperty(discount);
        });
        productsTotalPriceColumn.setCellValueFactory(cellData -> {
            Invoice_item invoiceItem = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(Math.round(invoiceItem.getFinal_total_price())));
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

    @FXML
    public void searchForInvoice(ActionEvent event) {
        String invoiceId = searchInvoiceIdField.getText();
        ArrayList<Invoice> invoices = new ArrayList<>() ;
        if (!invoiceId.isEmpty()){
            invoices.add(invoiceRepository.findById(Long.parseLong(invoiceId)));
        } else if (searchCustomerNameField.getText()!= null &&!searchCustomerNameField.getText().isEmpty()){
            invoices.addAll(invoiceRepository.findByCustomerName(searchCustomerNameField.getText()));
        } else if (searchDateField.getText()!= null &&!searchDateField.getText().isEmpty()){
            invoices = new ArrayList<>(getInvoicesByDate(searchDateField.getText()));
        }else{
            invoices.addAll(invoiceRepository.findAll());
        }
        invoiceTable.setItems(FXCollections.observableArrayList(invoices));
        searchInvoiceIdField.clear();
        searchCustomerNameField.clear();
        searchDateField.clear();
    }

    public ArrayList<Invoice> getInvoicesByDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date parsedDate = dateFormat.parse(date);
            String searchDate = dateFormat.format(parsedDate);
            List<Invoice> allInvoices = invoiceRepository.findAll();
            ArrayList<Invoice> invoices = new ArrayList<>();
            for (Invoice invoice : allInvoices) {
                Timestamp currentDate = invoice.getCreatedAt();
                PersianCalendar persianCalendar = new PersianCalendar();
                persianCalendar.setTime(currentDate);
                PersianDateFormat persianDateFormat = new PersianDateFormat("yyyy/MM/dd");
                String invoiceDate = persianDateFormat.format(persianCalendar);
                if (searchDate.equals(invoiceDate)) {
                    invoices.add(invoice);
                }
            }
            return new ArrayList<>(invoices);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
