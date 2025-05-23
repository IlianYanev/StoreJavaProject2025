package controller;

import model.Product;
import service.CustomerService;
import service.StoreService;
import view.StoreView;

import java.util.List;

public class CustomerController {
    private final CustomerService customerService;
    private final StoreService storeService;
    private final StoreView view;

    public CustomerController(CustomerService customerService, StoreService storeService, StoreView view) {
        this.customerService = customerService;
        this.storeService = storeService;
        this.view = view;
    }

    public void start() {
        boolean adding = true;

        while (adding) {
            view.print("\n--- Available Products ---");
            List<Product> products = customerService.getAvailableProducts();
            if (products.isEmpty()) {
                view.print("No available (non-expired) products.");
                return;
            }

            for (Product p : products) {
                double finalPrice = storeService.calculateSellingPrice(p);
                view.print(p.getId() + " | " + p.getName() + " | Price: " + finalPrice + " BGN | Quantity: " + p.getQuantity());
            }

            String productId = view.getInput("Enter product ID to add to cart or 0 to finish: ");
            if (productId.equals("0")) {
                view.print("Finished adding to cart.");
                break;
            }

            Product selected = customerService.findProductById(productId);
            if (selected == null) {
                view.print("Product not found or expired.");
                continue;
            }

            try {
                int qty = Integer.parseInt(view.getInput("Enter quantity: "));
                if (qty <= 0) throw new NumberFormatException();
                customerService.addToCart(selected, qty);
                view.print("Product added to cart.");
            } catch (NumberFormatException e) {
                view.print("Invalid quantity!");
            } catch (IllegalArgumentException e) {
                view.print("Error: " + e.getMessage());
            }
        }

        view.getInput("Press Enter to return...");
    }
    public void viewCart() {
        customerService.viewCart();
        view.getInput("Press Enter to return...");
    }

}
