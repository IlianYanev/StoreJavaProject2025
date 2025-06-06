package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Product implements Serializable {
    private String id;
    private String name;
    private double purchasePrice;
    private ProductCategory category;
    private LocalDate expirationDate;
    private int quantity;


    public Product(String id, String name, double purchasePrice, ProductCategory category, LocalDate expirationDate, int quantity) {
        this.id = id;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.category = category;
        this.expirationDate = expirationDate;
        this.quantity = quantity;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isExpired() {
        if (expirationDate == null) return false;
        return expirationDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Price: %.2f | Category: %s | Expiration: %s | Quantity: %d",
                id, name, purchasePrice, category, expirationDate, quantity);
    }
}