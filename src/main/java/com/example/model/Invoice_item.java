package com.example.model;

import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
@Table(name = "invoice_items", schema = "sand_box")
public class Invoice_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_item_id")
    private BigInteger invoice_item_id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private int unit_price;

    @Column(name = "discount", nullable = false)
    private double discount;

    @Column(name = "final_total_price", nullable = false)
    private int final_total_price;

    public BigInteger getInvoice_item_id() {
        return invoice_item_id;
    }

    public void setInvoice_item_id(BigInteger invoice_item_id) {
        this.invoice_item_id = invoice_item_id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getFinal_total_price() {
        return final_total_price;
    }

    public void setFinal_total_price(int final_total_price) {
        this.final_total_price = final_total_price;
    }
}
