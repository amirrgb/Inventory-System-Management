package com.example.model;

import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
@Table(name = "customers", schema = "sand_box")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private BigInteger customer_id;

    @Column(name = "first_name", columnDefinition = "VARCHAR(50)", nullable = true,unique = false)
    private String first_name;

    @Column(name = "last_name", columnDefinition = "VARCHAR(50)", nullable = true,unique = false)
    private String last_name;

    @Column(name = "phone_number", columnDefinition = "VARCHAR(50)", nullable = true, unique = true)
    private String phone_number;

    @Column(name = "email", columnDefinition = "VARCHAR(50)", nullable = true, unique = true)
    private String email;

    public BigInteger getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(BigInteger customer_id) {
        this.customer_id = customer_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
