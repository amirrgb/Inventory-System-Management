package com.example.controller;

import com.example.model.Customer;
import com.example.model.Invoice;
import com.example.repository.FeatureRepository;
import com.example.repository.InvoiceRepository;
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
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.example.iransandbox.SpringBootJavaFXApplication.applicationContext;

@Controller
public class ReportController {


    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private FeatureRepository featureRepository;

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

    private final ObservableList<Invoice> invoices = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        updateInvoices();
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

    }

    @FXML
    private void monthlyReport(ActionEvent event) {
        List<Invoice> invoices = invoiceRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        LocalDate startDate = currentDate.withDayOfMonth(1);  // Start of the current month
        LocalDate endDate = currentDate.with(TemporalAdjusters.lastDayOfMonth());  // End of the current month

        List<Invoice> filteredInvoices = filterInvoices(invoices, startDate, endDate);

        invoiceTable.setItems(FXCollections.observableArrayList(filteredInvoices));
        updateInvoices();
    }

    @FXML
    private void quarterlyReport(ActionEvent event) {
        List<Invoice> invoices = invoiceRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        int currentQuarter = (currentDate.getMonthValue() - 1) / 3 + 1;
        LocalDate startDate = currentDate.withMonth((currentQuarter - 1) * 3 + 1).withDayOfMonth(1);  // Start of the current quarter
        LocalDate endDate = startDate.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());  // End of the current quarter

        List<Invoice> filteredInvoices = filterInvoices(invoices, startDate, endDate);

        invoiceTable.setItems(FXCollections.observableArrayList(filteredInvoices));
        updateInvoices();
    }

    @FXML
    private void yearlyReport(ActionEvent event) {
        List<Invoice> invoices = invoiceRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        LocalDate startDate = currentDate.withDayOfYear(1);  // Start of the current year
        LocalDate endDate = currentDate.with(TemporalAdjusters.lastDayOfYear());  // End of the current year

        List<Invoice> filteredInvoices = filterInvoices(invoices, startDate, endDate);

        invoiceTable.setItems(FXCollections.observableArrayList(filteredInvoices));
        updateInvoices();
    }

    private List<Invoice> filterInvoices(List<Invoice> invoices, LocalDate startDate, LocalDate endDate) {
        List<Invoice> filteredInvoices = new ArrayList<>();
        for (Invoice invoice : invoices) {
            LocalDate invoiceDate = invoice.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if ((invoiceDate.isEqual(startDate) || invoiceDate.isAfter(startDate)) &&
                    (invoiceDate.isEqual(endDate) || invoiceDate.isBefore(endDate))) {
                filteredInvoices.add(invoice);
            }
        }
        return filteredInvoices;
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

}
