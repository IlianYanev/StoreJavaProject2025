package service;

import model.Product;
import service.ProductService;
import service.StoreService;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private final ProductService productService;
    private final StoreService storeService;
    private final List<Product> cart = new ArrayList<>();

    public CustomerService(ProductService productService, StoreService storeService) {
        this.productService = productService;
        this.storeService = storeService;
    }

    public List<Product> getAvailableProducts() {
        List<Product> all = productService.getAllProducts();
        List<Product> valid = new ArrayList<>();
        for (Product p : all) {
            if (!p.isExpired()) {
                valid.add(p);
            }
        }
        return valid;
    }

    public Product findProductById(String id) {
        return getAvailableProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addToCart(Product selected, int quantity) {
        if (quantity > selected.getQuantity()) {
            throw new IllegalArgumentException("Not enough quantity available.");
        }

        selected.setQuantity(selected.getQuantity() - quantity);

        Product copy = new Product(
                selected.getId(),
                selected.getName(),
                selected.getPurchasePrice(),
                selected.getCategory(),
                selected.getExpirationDate(),
                quantity
        );

        cart.add(copy);

        storeService.saveProductsToFile();
    }

    public List<Product> getCart() {
        return cart;
    }

    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("\n--- Cart Contents ---");
        double total = 0;

        for (Product p : cart) {
            double finalPrice = storeService.calculateSellingPrice(p);
            double totalItemPrice = finalPrice * p.getQuantity();
            total += totalItemPrice;

            System.out.printf("ID: %s | Name: %s | Qty: %d | Price: %.2f BGN | Total: %.2f BGN%n",
                    p.getId(), p.getName(), p.getQuantity(), finalPrice, totalItemPrice);
        }

        System.out.printf("Total: %.2f BGN%n", total);
    }

}
