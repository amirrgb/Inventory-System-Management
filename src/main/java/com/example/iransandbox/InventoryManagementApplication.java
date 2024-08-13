package com.example.iransandbox;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = "com.example")
@ComponentScan(basePackages = {"com.example.controller", "com.example.repository", "com.example.model"})
@EntityScan(basePackageClasses = com.example.model.Category.class)
public class InventoryManagementApplication {
    public static void main(String[] args) {
        SpringBootJavaFXApplication.main(args);
    }
}
