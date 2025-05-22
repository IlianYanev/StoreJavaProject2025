package controller;

import model.CashReg;
import model.Cashier;
import service.CashRegService;
import view.StoreView;

import java.util.List;

public class CashRegController {
    private final CashRegService cashRegService;
    private final StoreView view;

    public CashRegController(CashRegService cashRegService, StoreView view) {
        this.cashRegService = cashRegService;
        this.view = view;
    }

    public void manageCashRegisters() {
        while (true) {
            view.print("--- Manage Cash Registers ---");

            List<CashReg> registers = cashRegService.getCashRegisters();
            for (CashReg reg : registers) {
                view.print(reg.toString());
            }

            int choice;
            try {
                choice = Integer.parseInt(view.getInput("Choose a register number (1-6) to assign a cashier, or 0 to return: "));
            } catch (NumberFormatException e) {
                view.print("Invalid input! Please enter a number.");
                continue;
            }

            if (choice == 0) break;

            if (choice < 1 || choice > registers.size()) {
                view.print("Invalid register number!");
                continue;
            }

            List<Cashier> cashiers = cashRegService.getAllCashiers();
            if (cashiers.isEmpty()) {
                view.print("No cashiers available. Add some first.");
                continue;
            }

            view.print("Available Cashiers:");
            for (Cashier c : cashiers) {
                view.print("ID: " + c.getId() + " | Name: " + c.getName());
            }

            String cashierId = view.getInput("Enter cashier ID to assign to register or 0 to remove: ");
            if (cashierId.equals("0")) {
                boolean removed = cashRegService.removeCashierFromRegister(choice);
                if (removed) {
                    view.print("Cashier removed from register " + choice);
                } else {
                    view.print("Failed to remove cashier from register.");
                }
                continue;
            }

            boolean assigned = cashRegService.assignCashierToRegister(choice, cashierId);
            if (assigned) {
                view.print("Cashier assigned successfully.");
            } else {
                view.print("Cashier not found or invalid register number.");
            }
        }
    }
}
