package model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;


public class Store implements Serializable{
    private static final String CASHIER_FILE = "cashiers.dat";
    private List<Product> products;
    private List<Cashier> cashiers;
    private List<CashReg> cashRegisters;
    private double foodMarkupPercent = 10.0;     // default 20%
    private double nonFoodMarkupPercent = 20.0;
    private int daysBeforeExpirationDiscount = 3; // или стойност, която вече имаш
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

    // === Product Management ===

    public void addProduct(Product product) {
        products.add(product);
    }

    public String generateNextProductId() {
        Set<Integer> usedIds = new HashSet<>();
        for (Product p : products) {
            try {
                usedIds.add(Integer.parseInt(p.getId()));
            } catch (NumberFormatException e) {
                // Ако id-то не е число, го пропускаме
            }
        }

        int id = 1;
        while (usedIds.contains(id)) {
            id++;
        }
        return String.valueOf(id);
    }

    public double calculateSellingPrice(Product product) {
        double basePrice = product.getPurchasePrice();
        double markup = 0;

        if (product.getCategory() == ProductCategory.FOOD) {
            markup = foodMarkupPercent;
        } else {

            markup = nonFoodMarkupPercent;
        }

        double priceWithMarkup = basePrice + (basePrice * markup / 100);

        // Проверка за наближаващ срок на годност
        long daysToExpire = ChronoUnit.DAYS.between(LocalDate.now(), product.getExpirationDate());
        if (daysToExpire < daysBeforeExpirationDiscount) {
            priceWithMarkup *= (1 - expirationDiscountPercent / 100);
        }

        return Math.round(priceWithMarkup * 100.0) / 100.0; // закръгляне до 2 знака
    }

    public String generateNextCashierId() {
        Set<Integer> usedIds = new HashSet<>();
        for (Cashier c : cashiers) {
            try {
                usedIds.add(Integer.parseInt(c.getId()));
            } catch (NumberFormatException e) {
                // Ако ID-то не е число, пропускаме
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

    public Product getProductById(String id) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void saveProductsToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(products);
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
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




    // === Cashier Management ===

    public void addCashier(Cashier cashier) {
        cashiers.add(cashier);
        saveCashiersToFile(Store.CASHIER_FILE);
        // запис при добавяне
    }

    public boolean removeCashierById(String id) {
        boolean removed = cashiers.removeIf(c -> c.getId().equals(id));
        if (removed) saveCashiersToFile(); // запис при премахване
        return removed;
    }


    public List<Cashier> getAllCashiers() {
        return cashiers;
    }

    public Cashier getCashierById(String id) {
        for (Cashier c : cashiers) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void saveCashiersToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(cashiers);
        } catch (IOException e) {
            System.out.println("Error saving cashiers: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
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

    // 👇 ТЕЗИ два метода са ключови!
    public void saveCashiersToFile() {
        saveCashiersToFile(CASHIER_FILE);
    }

    public void loadCashiersFromFile() {
        loadCashiersFromFile(CASHIER_FILE);
    }

    public List<CashReg> getCashRegisters() {
        return cashRegisters;
    }


}
