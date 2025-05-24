package service;

import model.Store;
import model.Product;
import model.Cashier;
import model.CashReg;
import model.ProductCategory;
import view.StoreView;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StoreService {
    private final Store store;
    private final StoreView view;
    private final String CASHIER_FILE = "src/cashiers.txt";
    private final String PRODUCT_FILE = "src/products.txt";

    public StoreService(Store store, StoreView view) {
        this.store = store;
        this.view = view;
    }

    public String generateNextProductId() {
        Set<Integer> usedIds = new HashSet<>();
        for (Product p : store.getAllProducts()) {
            try { usedIds.add(Integer.parseInt(p.getId())); } catch (NumberFormatException ignored) {}
        }
        int id = 1;
        while (usedIds.contains(id)) id++;
        return String.valueOf(id);
    }

    public String generateNextCashierId() {
        Set<Integer> usedIds = new HashSet<>();
        for (Cashier c : store.getAllCashiers()) {
            try { usedIds.add(Integer.parseInt(c.getId())); } catch (NumberFormatException ignored) {}
        }
        int id = 1;
        while (usedIds.contains(id)) id++;
        return String.valueOf(id);
    }

    public double calculateSellingPrice(Product product) {
        double basePrice = product.getPurchasePrice();
        double markup = (product.getCategory() == ProductCategory.FOOD) ?
                store.getFoodMarkupPercent() : store.getNonFoodMarkupPercent();

        double price = basePrice + (basePrice * markup / 100);

        if (product.getExpirationDate() != null) {
            long daysToExpire = ChronoUnit.DAYS.between(LocalDate.now(), product.getExpirationDate());
            if (daysToExpire < store.getDaysBeforeExpirationDiscount()) {
                price *= (1 - store.getExpirationDiscountPercent() / 100);
            }
        }

        return Math.round(price * 100.0) / 100.0;
    }


    public void saveProductsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCT_FILE))) {
            oos.writeObject(store.getAllProducts());
        } catch (IOException e) {
            view.print("Error saving products: " + e.getMessage());
        }
    }

    public void loadProductsFromFile() {
        File file = new File(PRODUCT_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Product> products = (List<Product>) ois.readObject();
            store.getAllProducts().clear();
            store.getAllProducts().addAll(products);
        } catch (IOException | ClassNotFoundException e) {
            view.print("Error loading products: " + e.getMessage());
        }
    }

    public void saveCashiersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CASHIER_FILE))) {
            oos.writeObject(store.getAllCashiers());
        } catch (IOException e) {
            view.print("Error saving cashiers: " + e.getMessage());
        }
    }

    public void loadCashiersFromFile() {
        File file = new File(CASHIER_FILE);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Cashier> cashiers = (List<Cashier>) ois.readObject();
            store.getAllCashiers().clear();
            store.getAllCashiers().addAll(cashiers);
        } catch (IOException | ClassNotFoundException e) {
            view.print("Error loading cashiers: " + e.getMessage());
        }
    }

    public List<CashReg> getCashRegisters() {
        return store.getCashRegisters();
    }
}
