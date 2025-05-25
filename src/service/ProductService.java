package service;

import model.Product;
import model.ProductCategory;
import model.Store;

import java.time.LocalDate;
import java.util.List;

public class ProductService {
    private final Store store;
    private final String productFilePath;

    public ProductService(Store store, String productFilePath) {
        this.store = store;
        this.productFilePath = productFilePath;

    }

    public Product createProduct(String name, double price, ProductCategory category,
                                 LocalDate expiration, int quantity) {
        String id = store.generateNextProductId();
        return new Product(id, name, price, category, expiration, quantity);
    }

    public void addProduct(Product product) {
        store.addProduct(product);
        store.saveProductsToFile(productFilePath);
    }

    public List<Product> getAllProducts() {
        store.loadProductsFromFile(productFilePath);
        return store.getAllProducts();
    }

    public boolean removeProductById(String id) {
        store.loadProductsFromFile(productFilePath);
        boolean removed = store.removeProductById(id);
        if (removed) {
            store.saveProductsToFile(productFilePath);
        }
        return removed;
    }
}