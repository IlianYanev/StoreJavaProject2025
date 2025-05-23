package controller;

import model.Cashier;
import service.CashierService;
import view.StoreView;

import java.util.List;

public class CashierController {
    private final CashierService cashierService;
    private final StoreView view;

    public CashierController(CashierService cashierService, StoreView view) {
        this.cashierService = cashierService;
        this.view = view;
    }

    public void addCashier() {
        String name = view.getInput("Enter cashier name: ");
        double salary;
        while (true) {
            try {
                salary = Double.parseDouble(view.getInput("Enter salary: "));
                if (salary < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                view.print("Invalid salary! Please enter a positive number.");
            }
        }
        Cashier c = cashierService.createCashier(name, salary);
        cashierService.addCashier(c);
        view.print("Cashier added.");
    }

    public void viewCashiers() {
        boolean back = false;
        while (!back) {
            view.print("--- Cashiers List ---");
            List<Cashier> cashiers = cashierService.getAllCashiers();
            if (cashiers.isEmpty()) {
                view.print("No cashiers available.");
                back = true;
                continue;
            }
            for (Cashier c : cashiers) {
                view.print("ID: " + c.getId() + " | " + c.getName() + " | " + c.getSalary() + " BGN");
            }
            String input = view.getInput("Enter 9 to go back: ");
            if (input.equals("9")) {
                back = true;
            }
        }
    }

    public void removeCashier() {
        List<Cashier> cashiers = cashierService.getAllCashiers();
        if (cashiers.isEmpty()) {
            view.print("No cashiers available.");
            return;
        }
        view.print("--- Cashiers List ---");
        for (Cashier c : cashiers) {
            view.print("ID: " + c.getId() + " | " + c.getName() + " | " + c.getSalary() + " BGN");
        }
        String id = view.getInput("Enter cashier ID to remove: ");
        boolean removed = cashierService.removeCashierById(id);
        if (removed) {
            view.print("Cashier removed successfully.");
        } else {
            view.print("No cashier found with that ID.");
        }
    }
}