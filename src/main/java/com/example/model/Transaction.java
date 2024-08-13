package com.example.model;

import com.example.iransandbox.SpringBootJavaFXApplication;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "transactions", schema = "sand_box")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private BigInteger transaction_id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "type", columnDefinition = "ENUM('deposit','withdraw','set')")
    private String type;


    @Column(name = "timestamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp timestamp;

    public BigInteger getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(BigInteger transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public static Transaction makeTransaction(Product product,int quantity, String type) {
        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setUser(SpringBootJavaFXApplication.globalUserRepository.findUserByUsername(SpringBootJavaFXApplication.currentUser));
        transaction.setQuantity(quantity);
        transaction.setType(type);
        Date date = new Date();
        transaction.setTimestamp(new Timestamp(date.getTime()));
        return transaction;
    }
}
