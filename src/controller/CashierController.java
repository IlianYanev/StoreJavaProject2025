package controller;

import model.Cashier;
import model.Store;
import view.StoreView;

public class CashierController {
    private final Store store;
    private final StoreView view;

    public CashierController(Store store, StoreView view) {
        this.store = store;
        this.view = view;
    }

    public void addCashier() {
        String id = store.generateNextCashierId();
        String name = view.getInput("Enter cashier name: ");
        double salary = Double.parseDouble(view.getInput("Enter salary: "));

        Cashier c = new Cashier(id, name, salary);
        store.getAllCashiers().add(c);
        store.saveCashiersToFile("cashiers.txt");
        view.print("Cashier added.");
    }

    public void viewCashiers() {
        boolean back = false;
        while (!back) {
            view.print("--- Cashiers List ---");
            for (Cashier c : store.getAllCashiers()) {
                view.print("ID: " + c.getId() + " | " + c.getName() + " | " + c.getSalary() + "BGN" );
            }
            String input = view.getInput("Enter 9 to go back: ");
            if (input.equals("9")) {
                back = true;
            }
        }
    }

    public void removeCashier() {
        view.print("--- Cashiers List ---");
        for (Cashier c : store.getAllCashiers()) {
            view.print("ID: " + c.getId() + " | " + c.getName() + " | " + c.getSalary() + "BGN" );
        }
        String id = view.getInput("Enter cashier ID to remove: ");
        boolean removed = store.getAllCashiers().removeIf(c -> c.getId().equals(id));
        if (removed) {
            view.print("Cashier removed successfully.");
            store.saveCashiersToFile();  // Записваме промяната във файла!
        } else {
            view.print("No cashier found with that ID.");
        }
    }

}
