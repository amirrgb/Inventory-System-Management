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

    @Column(name = "address", columnDefinition = "VARCHAR(100)", nullable = true)
    private String address;

    @Column(name = "city", columnDefinition = "VARCHAR(50)", nullable = true)
    private String city;

    @Column(name = "state", columnDefinition = "VARCHAR(50)", nullable = true)
    private String state;

    @Column(name = "national_id", columnDefinition = "VARCHAR(50)", nullable = true)
    private String national_id;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }
}
