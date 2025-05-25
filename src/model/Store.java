package model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;


public class Store implements Serializable{
    private static final String CASHIER_FILE = "src/cashiers.txt";
    private List<Product> products;
    private List<Cashier> cashiers;
    private final List<CashReg> cashRegisters;
    private double foodMarkupPercent = 10.0;
    private double nonFoodMarkupPercent = 20.0;
    private int daysBeforeExpirationDiscount = 3;
    private double expirationDiscountPercent = 10.0;



    public int getDaysBeforeExpirationDiscount() {
        return daysBeforeExpirationDiscount;
    }

    public void setDaysBeforeExpirationDiscount(int daysBeforeExpirationDiscount) {
        this.daysBeforeExpirationDiscount = daysBeforeExpirationDiscount;
    }

    public double getExpirationDiscountPercent() {
        return expirationDiscountPercent;
    }

    public void setExpirationDiscountPercent(double expirationDiscountPercent) {
        this.expirationDiscountPercent = expirationDiscountPercent;
    }

    public double getFoodMarkupPercent() {
        return foodMarkupPercent;
    }

    public void setFoodMarkupPercent(double foodMarkupPercent) {
        this.foodMarkupPercent = foodMarkupPercent;
    }

    public double getNonFoodMarkupPercent() {
        return nonFoodMarkupPercent;
    }

    public void setNonFoodMarkupPercent(double nonFoodMarkupPercent) {
        this.nonFoodMarkupPercent = nonFoodMarkupPercent;
    }


    public Store() {
        products = new ArrayList<>();
        cashiers = new ArrayList<>();
        cashRegisters = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            cashRegisters.add(new CashReg(i));
        }

        loadCashiersFromFile(CASHIER_FILE);
    }


    public void addProduct(Product product) {
        products.add(product);
    }

    public String generateNextProductId() {
        Set<Integer> usedIds = new HashSet<>();
        for (Product p : products) {
            try {
                usedIds.add(Integer.parseInt(p.getId()));
            } catch (NumberFormatException e) {

            }
        }

        int id = 1;
        while (usedIds.contains(id)) {
            id++;
        }
        return String.valueOf(id);
    }

    public String generateNextCashierId() {
        Set<Integer> usedIds = new HashSet<>();
        for (Cashier c : cashiers) {
            try {
                usedIds.add(Integer.parseInt(c.getId()));
            } catch (NumberFormatException e) {
            }
        }

        int id = 1;
        while (usedIds.contains(id)) {
            id++;
        }

        return String.valueOf(id);
    }

    public boolean removeProductById(String id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public void saveProductsToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(products);
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    public void loadProductsFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No product file found. Starting with empty list.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.products = (List<Product>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    public List<Cashier> getAllCashiers() {
        return cashiers;
    }

    public void saveCashiersToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(cashiers);
        } catch (IOException e) {
            System.out.println("Error saving cashiers: " + e.getMessage());
        }
    }

    public void loadCashiersFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No cashier file found. Starting with empty list.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            this.cashiers = (List<Cashier>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading cashiers: " + e.getMessage());
        }
    }

    public List<CashReg> getCashRegisters() {
        return cashRegisters;
    }

}