package com.example.model;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "invoices", schema = "sand_box")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private BigInteger invoice_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "total_amount", nullable = false)
    private int total_amount;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Invoice_item> invoiceItems;

    public BigInteger getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(BigInteger invoice_id) {
        this.invoice_id = invoice_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Invoice_item> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(Set<Invoice_item> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }
}
