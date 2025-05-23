package service;

import model.Product;
import model.ProductCategory;
import model.Store;

import java.time.LocalDate;
import java.util.List;

public class ProductService {
    private final Store store;

    public ProductService(Store store) {
        this.store = store;
    }

    public Product createProduct(String name, double price, ProductCategory category,
                                 LocalDate expiration, int quantity) {
        String id = store.generateNextProductId();
        return new Product(id, name, price, category, expiration, quantity);
    }

    public void addProduct(Product product) {
        store.addProduct(product);
        store.saveProductsToFile("src/products.txt");
    }

    public List<Product> getAllProducts() {
        store.loadProductsFromFile("src/products.txt");
        return store.getAllProducts();
    }

    public boolean removeProductById(String id) {
        store.loadProductsFromFile("src/products.txt");
        boolean removed = store.removeProductById(id);
        if (removed) {
            store.saveProductsToFile("src/products.txt");
        }
        return removed;
    }
}