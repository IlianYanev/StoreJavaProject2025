package service;

import jdk.management.jfr.RecordingInfo;
import model.Product;
import model.CashReg;
import service.ReceiptService;
import service.ProductService;
import service.StoreService;
import view.StoreView;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private final ProductService productService;
    private final StoreService storeService;
    private final List<Product> cart = new ArrayList<>();
    private final ReceiptService receiptService;
    private final StoreView view;


    public CustomerService(ProductService productService, StoreService storeService, StoreView view) {
        this.productService = productService;
        this.storeService = storeService;
        this.view = view;
        this.receiptService = new ReceiptService();
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

    public void checkout() {
        if (cart.isEmpty()) {
            view.print("Cart is empty. Nothing to checkout.");
            return;
        }

        List<CashReg> registers = storeService.getCashRegisters();
        List<CashReg> active = new ArrayList<>();

        view.print("\n--- Available Cash Registers ---");
        for (CashReg reg : registers) {
            if (reg.getCashier() != null) {
                view.print(reg.getNumber() + ". " + reg.getCashier().getName() + " (ID: " + reg.getCashier().getId() + ")");
                active.add(reg);
            }
        }

        if (active.isEmpty()) {
            view.print("No cash register working.");
            return;
        }

        CashReg selectedRegister = null;

        while (selectedRegister == null) {
            try {
                int chosenNumber = Integer.parseInt(view.getInput("Select register number: "));
                CashReg potential = storeService.getCashRegisters().get(chosenNumber - 1);
                if (potential.getCashier() == null) {
                    view.print("This register has no cashier!");
                } else {
                    selectedRegister = potential;
                }
            } catch (Exception e) {
                view.print("Invalid input.");
            }
        }

        double total = 0;
        for (Product p : cart) {
            total += storeService.calculateSellingPrice(p) * p.getQuantity();
        }

        view.print(String.format("Total to pay: %.2f BGN", total));

        double payment;
        try {
            payment = Double.parseDouble(view.getInput("Enter payment amount: "));
        } catch (Exception e) {
            view.print("Invalid amount.");
            return;
        }

        if (payment < total) {
            view.print("Not enough money. Returning to menu...");
            return;
        }

        receiptService.printAndSaveReceipt(cart, selectedRegister.getCashier(), payment, total);
        view.print("Purchase completed. Thank you!");
        cart.clear();
    }




}
