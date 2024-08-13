package com.example.iransandbox;

import com.example.repository.UserRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.example")
@ComponentScan(basePackages = {"com.example.controller", "com.example.repository", "com.example.model"})
@EntityScan(basePackageClasses = {
        com.example.model.Category.class,com.example.model.Category.class,
        com.example.model.Category.class,com.example.model.Category.class,
        com.example.model.Category.class})

public class SpringBootJavaFXApplication extends Application {

    public static ConfigurableApplicationContext applicationContext;
    public static String currentUser;
    public static String currentRole;
    public static UserRepository globalUserRepository;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(SpringBootJavaFXApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/user.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
