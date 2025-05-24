package controller;

import model.Product;
import model.ProductCategory;
import service.ProductService;
import view.StoreView;

import java.time.LocalDate;
import java.util.List;

public class ProductController {
    private final ProductService productService;
    private final StoreView view;

    public ProductController(ProductService productService, StoreView view) {
        this.productService = productService;
        this.view = view;
    }

    public void addProduct() {
        view.print("--- Add Product ---");

        String name = view.getInput("Enter product name: ");

        double price;
        while (true) {
            try {
                price = Double.parseDouble(view.getInput("Enter purchase price: "));
                if (price < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                view.print("Invalid price! Please enter a positive number.");
            }
        }

        ProductCategory category = null;
        while (category == null) {
            String catInput = view.getInput("Enter category (FOOD / NON_FOOD): ").toUpperCase();
            try {
                category = ProductCategory.valueOf(catInput);
            } catch (IllegalArgumentException e) {
                view.print("Invalid category. Please enter FOOD or NON_FOOD.");
            }
        }

        LocalDate expiration = null;

        if (category == ProductCategory.FOOD) {
            while (expiration == null) {
                try {
                    String input = view.getInput("Enter expiration date (YYYY-MM-DD): ");
                    expiration = LocalDate.parse(input);
                } catch (Exception e) {
                    view.print("Invalid date format. Please enter date as YYYY-MM-DD.");
                }
            }
        } else {
            String response = view.getInput("Does this product have an expiration date? (y/n): ").toLowerCase();
            if (response.equals("y")) {
                while (expiration == null) {
                    try {
                        String input = view.getInput("Enter expiration date (YYYY-MM-DD): ");
                        expiration = LocalDate.parse(input);
                    } catch (Exception e) {
                        view.print("Invalid date format. Please enter date as YYYY-MM-DD.");
                    }
                }
            }
        }

        int quantity;
        while (true) {
            try {
                quantity = Integer.parseInt(view.getInput("Enter quantity: "));
                if (quantity < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                view.print("Invalid quantity! Please enter a non-negative integer.");
            }
        }

        Product p = productService.createProduct(name, price, category, expiration, quantity);
        productService.addProduct(p);
        view.print("Product added successfully.");
    }


    public void viewAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            view.print("No products available.");
        } else {
            view.print("--- Product List ---");
            for (Product p : products) {
                String status = p.isExpired() ? " (EXPIRED)" : "";
                view.print(p.toString() + status);
            }
        }
        view.getInput("Press Enter to return to menu...");
    }

    public void removeProduct() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            view.print("No products available.");
        } else {
            view.print("--- Product List ---");
            for (Product p : products) {
                view.print(p.toString());
            }
        }

        view.print("--- Remove Product ---");
        String id = view.getInput("Enter product ID to remove: ");
        boolean removed = productService.removeProductById(id);
        if (removed) {
            view.print("Product removed successfully.");
        } else {
            view.print("Product with given ID not found.");
        }
        view.getInput("Press Enter to return to menu...");
    }
}