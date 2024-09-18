# Inventory Management System

An advanced inventory management system designed to simplify and automate stock tracking, invoicing, and reporting. Built using **Java** with **JavaFX** for the front end and **Spring** for backend operations. This system supports Persian (Jalali) date handling for use in local businesses.

## Features

### 1. **Inventory Management**
- Categorize and manage products across different categories.
- Add products dynamically with the ability to assign attributes to each category.
- Track product quantities in warehouses in real-time.

### 2. **Invoicing System**
- Create sales invoices with customer and employee details.
- Automatically generate invoices for each sale.
- Manage invoices, including updating or removing them.

### 3. **Reports**
- Generate monthly, quarterly, and yearly reports of sales.
- View detailed transactions for each report.
- Visual sales charts to represent data for each period (Monthly, Quarterly, Yearly).
  
### 4. **Access Control**
- Different access levels for admin, managers, and employees.
- Role-based permissions to ensure security in operations.

### 5. **Sales Tracking**
- Monitor and track sales transactions.
- Automatically update inventory quantities after each sale.
- Support for multiple sales channels (can be extended in the future).

### 6. **Custom Persian Calendar (Jalali)**
- Persian (Jalali) calendar integration for handling dates in reports and invoices.
- Utilizes Qasem Kiani's PersianCalendar for accurate date conversion and manipulation.

### 7. **User Management**
- Add, edit, or delete users.
- Assign roles to users (Admin, Manager, Employee).
  
### 8. **Dynamic Product Management**
- Add new products with different attributes based on category.
- Flexible inventory management with support for bulk updates.

### 9. **Transaction & Invoice Tracking**
- Easily track and manage all sales transactions.
- Invoice summary and details for each transaction.

## Technologies Used

- **Java**: Core programming language.
- **JavaFX**: For building the user interface.
- **Spring Framework**: Backend framework for business logic, dependency injection, and database management.
- **MySQL/PostgreSQL**: Database management system for storing user and transaction data.
- **PersianCalendar (Qasem Kiani)**: Handling Persian (Jalali) calendar dates in the application.

## Setup & Installation

### Prerequisites
- **Java 8+** installed.
- **Maven/Gradle** installed.
- A running instance of **MySQL/PostgreSQL** for the database.

### Installation Steps

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/Inventory-System-Management.git
    cd Inventory-System-Management
    ```

2. Update `application.properties` with your database credentials:
    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/inventory
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    ```

3. Build and run the project:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

4. Run the JavaFX application:
    ```bash
    java -jar target/inventory-management-system.jar
    ```

### Usage

1. **Admin Access**
    - Add and manage users, set roles, and access all data.
  
2. **Manager Access**
    - View and manage reports, track sales, and manage products and inventory.
  
3. **Employee Access**
    - Create invoices, manage customer data, and update product quantities.

4. **Reports & Analysis**
    - Go to the Reports section in the system to generate monthly, quarterly, and yearly sales data.
    - View real-time sales charts and detailed transaction reports.

## Benefits

- **Efficient Inventory Management**: Automate stock tracking and minimize manual errors.
- **Localized Date Support**: Seamlessly work with Jalali dates, making it perfect for local businesses.
- **Easy Reporting**: Generate visual and detailed reports for any given period, with built-in charts.
- **User Roles**: Secure role-based access ensures data privacy and integrity.
- **Real-Time Updates**: Inventory and sales data are updated in real-time, improving business decision-making.
  
## Future Enhancements

- **Multi-Store Support**: Manage multiple warehouses or store locations from a single system.
- **Mobile Version**: Develop a mobile application for on-the-go inventory management.
- **Analytics & Insights**: Adding a more in-depth analytics dashboard with predictive insights.
  
## Contribution

Feel free to fork this repository, create a feature branch, and submit a pull request. Any contributions, suggestions, or bug reports are appreciated.

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
