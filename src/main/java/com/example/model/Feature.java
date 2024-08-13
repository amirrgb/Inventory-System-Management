package com.example.model;

import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
@Table(name = "features", schema = "sand_box")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private BigInteger feature_id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "feature", columnDefinition = "VARCHAR(100)", nullable = false)
    private String feature;

    @Column(name = "feature_type", columnDefinition = "VARCHAR(100)", nullable = false)
    private String feature_type;

    @Column(name = "feature_value", columnDefinition = "VARCHAR(100)", nullable = true)
    private String feature_value;

    public BigInteger getFeature_id() {
        return feature_id;
    }

    public void setFeature_id(BigInteger feature_id) {
        this.feature_id = feature_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getFeature_type() {
        return feature_type;
    }

    public void setFeature_type(String feature_type) {
        this.feature_type = feature_type;
    }

    public String getFeature_value() {
        return feature_value;
    }

    public void setFeature_value(String feature_value) {
        this.feature_value = feature_value;
    }
}
