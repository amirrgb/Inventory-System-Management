package com.example.controller;

import com.example.model.Customer;
import com.example.model.Invoice;
import com.example.repository.FeatureRepository;
import com.example.repository.InvoiceRepository;
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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

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

    @FXML
    private LineChart<String, Number> salesChart;

    private final ObservableList<Invoice> invoices = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        updateInvoices();
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

        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTime(new Date()); // Set to current date

        // Set to the first day of the current month
        persianCalendar.set(PersianCalendar.DAY_OF_MONTH, 1);
        PersianCalendar startDate = new PersianCalendar();  // Create a new instance
        startDate.set(persianCalendar.get(PersianCalendar.YEAR),
                persianCalendar.get(PersianCalendar.MONTH),
                persianCalendar.get(PersianCalendar.DAY_OF_MONTH));

        // Set to the last day of the current month
        persianCalendar.set(PersianCalendar.DAY_OF_MONTH, persianCalendar.getActualMaximum(PersianCalendar.DAY_OF_MONTH));
        PersianCalendar endDate = new PersianCalendar();  // Create a new instance
        endDate.set(persianCalendar.get(PersianCalendar.YEAR),
                persianCalendar.get(PersianCalendar.MONTH),
                persianCalendar.get(PersianCalendar.DAY_OF_MONTH));

        List<Invoice> filteredInvoices = filterInvoices(invoices, startDate, endDate);

        invoiceTable.setItems(FXCollections.observableArrayList(filteredInvoices));
        generateSalesChart(filteredInvoices, "monthly");
        updateInvoices();
    }


    @FXML
    private void quarterlyReport(ActionEvent event) {
        List<Invoice> invoices = invoiceRepository.findAll();

        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTime(new Date()); // Set to current date

        int currentMonth = persianCalendar.get(PersianCalendar.MONTH);
        int currentQuarter = (currentMonth / 3) * 3; // Determine the start month of the current quarter

        // Set to the first day of the first month of the current quarter
        persianCalendar.set(PersianCalendar.MONTH, currentQuarter);
        persianCalendar.set(PersianCalendar.DAY_OF_MONTH, 1);
        PersianCalendar startDate = new PersianCalendar();  // Create a new instance
        startDate.set(persianCalendar.get(PersianCalendar.YEAR),
                persianCalendar.get(PersianCalendar.MONTH),
                persianCalendar.get(PersianCalendar.DAY_OF_MONTH));

        // Set to the last day of the last month of the current quarter
        persianCalendar.set(PersianCalendar.MONTH, currentQuarter + 2);
        persianCalendar.set(PersianCalendar.DAY_OF_MONTH, persianCalendar.getActualMaximum(PersianCalendar.DAY_OF_MONTH));
        PersianCalendar endDate = new PersianCalendar();  // Create a new instance
        endDate.set(persianCalendar.get(PersianCalendar.YEAR),
                persianCalendar.get(PersianCalendar.MONTH),
                persianCalendar.get(PersianCalendar.DAY_OF_MONTH));

        List<Invoice> filteredInvoices = filterInvoices(invoices, startDate, endDate);

        invoiceTable.setItems(FXCollections.observableArrayList(filteredInvoices));
        generateSalesChart(filteredInvoices, "quarterly");
        updateInvoices();
    }

    @FXML
    private void yearlyReport(ActionEvent event) {
        List<Invoice> invoices = invoiceRepository.findAll();

        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTime(new Date()); // Set to current date

        // Set to the first day of the year
        persianCalendar.set(PersianCalendar.MONTH, PersianCalendar.FARVARDIN);
        persianCalendar.set(PersianCalendar.DAY_OF_MONTH, 1);
        PersianCalendar startDate = new PersianCalendar();  // Create a new instance
        startDate.set(persianCalendar.get(PersianCalendar.YEAR),
                persianCalendar.get(PersianCalendar.MONTH),
                persianCalendar.get(PersianCalendar.DAY_OF_MONTH));

        // Set to the last day of the year
        persianCalendar.set(PersianCalendar.MONTH, PersianCalendar.ESFAND);
        persianCalendar.set(PersianCalendar.DAY_OF_MONTH, persianCalendar.getActualMaximum(PersianCalendar.DAY_OF_MONTH));
        PersianCalendar endDate = new PersianCalendar();  // Create a new instance
        endDate.set(persianCalendar.get(PersianCalendar.YEAR),
                persianCalendar.get(PersianCalendar.MONTH),
                persianCalendar.get(PersianCalendar.DAY_OF_MONTH));

        List<Invoice> filteredInvoices = filterInvoices(invoices, startDate, endDate);

        invoiceTable.setItems(FXCollections.observableArrayList(filteredInvoices));
        generateSalesChart(filteredInvoices, "yearly");
        updateInvoices();
    }

    private List<Invoice> filterInvoices(List<Invoice> invoices, PersianCalendar startDate, PersianCalendar endDate) {
        List<Invoice> filteredInvoices = new ArrayList<>();
        for (Invoice invoice : invoices) {
            PersianCalendar invoiceDate = new PersianCalendar();
            invoiceDate.setTime(invoice.getCreatedAt());

            if ((invoiceDate.equals(startDate) || invoiceDate.after(startDate)) &&
                    (invoiceDate.equals(endDate) || invoiceDate.before(endDate))) {
                filteredInvoices.add(invoice);
            }
        }
        filteredInvoices.sort(Comparator.comparing(Invoice::getCreatedAt));

        return filteredInvoices;
    }



    public Map<String, Double> categorizeInvoicesBasedOnDate(List<Invoice> invoices) {
        Map<String, Double> invoiceMap = new HashMap<>();
        String invoiceDate;
        for (Invoice invoice : invoices) {
            Timestamp currentDate = invoice.getCreatedAt();
            PersianCalendar persianCalendar = new PersianCalendar();
            persianCalendar.setTime(currentDate);
            PersianDateFormat persianDateFormat = new PersianDateFormat("yyyy/MM/dd");
            invoiceDate = persianDateFormat.format(persianCalendar);

            if (!invoiceMap.containsKey(invoiceDate)) {
                invoiceMap.put(invoiceDate, invoice.getTotal_amount());
            } else {
                invoiceMap.put(invoiceDate, invoiceMap.get(invoiceDate) + invoice.getTotal_amount());
            }
        }

        // Sort the map by keys (dates)
        return new TreeMap<>(invoiceMap);
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
    public void generateSalesChart(List<Invoice> invoices, String name) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(name);

        Map<String, Double> invoiceMap = categorizeInvoicesBasedOnDate(invoices);
        for (Map.Entry<String, Double> entry : invoiceMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        salesChart.getData().clear();
        salesChart.getData().add(series);
    }
}
