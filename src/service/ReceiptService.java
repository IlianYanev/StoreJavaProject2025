package service;

import model.Product;
import model.Cashier;
import model.Store;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReceiptService {
    private final String receiptFolder;

    public ReceiptService(String receiptFolder) {
        this.receiptFolder = receiptFolder;
    }

    public void printAndSaveReceipt(List<Product> cart, Cashier cashier, double totalPaid, double totalCost) {
        if (cart.isEmpty() || cashier == null) return;

        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int receiptNumber = getNextReceiptNumber();
        String filename = receiptFolder + "receipt_" + receiptNumber + ".txt";

        StringBuilder sb = new StringBuilder();
        System.out.println();
        sb.append("=== RECEIPT #").append(receiptNumber).append(" ===\n");
        sb.append("Date/Time: ").append(timestamp).append("\n");
        sb.append("Cashier: ").append(cashier.getName()).append(" (ID: ").append(cashier.getId()).append(")\n\n");

        for (Product p : cart) {
            double price = p.getPurchasePrice();
            sb.append(String.format("%s | Qty: %d | Unit Price: %.2f BGN | Total: %.2f BGN\n",
                    p.getName(), p.getQuantity(), price, price * p.getQuantity()));
        }

        sb.append("\n------------------------\n");
        sb.append(String.format("Total Cost: %.2f BGN\n", totalCost));
        sb.append(String.format("Paid: %.2f BGN\n", totalPaid));
        sb.append(String.format("Change: %.2f BGN\n", totalPaid - totalCost));
        sb.append("========================\n");

        System.out.println(sb);

        try {
            File dir = new File(receiptFolder);
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter(filename);
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing receipt: " + e.getMessage());
        }
    }

    private int getNextReceiptNumber() {
        File dir = new File(receiptFolder);
        if (!dir.exists()) return 1;

        File[] files = dir.listFiles((d, name) -> name.matches("receipt_\\d+\\.txt"));
        if (files == null || files.length == 0) return 1;

        int max = 0;
        for (File file : files) {
            String name = file.getName().replace("receipt_", "").replace(".txt", "");
            try {
                int num = Integer.parseInt(name);
                if (num > max) max = num;
            } catch (NumberFormatException ignored) {}
        }

        return max + 1;
    }

    public void viewAllReceipts() {
        File dir = new File(receiptFolder);
        if (!dir.exists() || dir.listFiles() == null) {
            System.out.println("No receipts found.");
            return;
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.out.println("No receipts found.");
            return;
        }

        List<File> sorted = Arrays.stream(files)
                .sorted(Comparator.comparing(File::getName))
                .toList();

        System.out.println("\n+--------------- Receipts List -------------+");
        System.out.println("|   #   |     Date     |     Total (BGN)    |");
        System.out.println("+-------------------------------------------+");

        int index = 1;
        for (File file : sorted) {
            String total = "N/A";
            String date = "N/A";
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Date/Time:")) {
                        date = line.replace("Date/Time:", "").trim();
                    } else if (line.startsWith("Total Cost:")) {
                        total = line.replace("Total Cost:", "").trim();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading " + file.getName());
            }

            System.out.printf("| %d | %s | %-14s \n", index, date, total);
            index++;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select receipt number to view full details (0 to go back): ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice < 1 || choice > sorted.size()) {
            if (choice != 0) {
                System.out.println("No such receipt.");
            }
            return;
        }

        File selected = sorted.get(choice - 1);

        try (BufferedReader reader = new BufferedReader(new FileReader(selected))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading receipt.");
        }
    }

    public void generateStoreReport(Store store, StoreService storeService) {
        double revenue = 0;
        double cost = 0;

        File dir = new File(receiptFolder);
        if (!dir.exists()) {
            System.out.println("No receipts for report.");
            return;
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files == null) {
            System.out.println("No receipts found.");
            return;
        }

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Total Cost:")) {
                        revenue += Double.parseDouble(line.split(":")[1].trim().replace(" BGN", ""));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading receipt: " + file.getName());
            }
        }

        for (Cashier c : store.getAllCashiers()) {
            cost += c.getSalary();
        }

        for (Product p : store.getAllProducts()) {
            cost += p.getPurchasePrice() * p.getQuantity();
        }

        double profit = revenue - cost;

        System.out.printf("Revenue: %.2f BGN\n", revenue);
        System.out.printf("Costs (salaries + stock): %.2f BGN\n", cost);
        System.out.printf("Profit: %.2f BGN\n", profit);
    }
}
