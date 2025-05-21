package controller;

import model.CashReg;
import model.Cashier;
import model.Store;
import view.StoreView;

import java.util.List;

public class CashRegController {
    private Store store;
    private StoreView view;

    public CashRegController(Store store, StoreView view) {
        this.store = store;
        this.view = view;
    }

    public void manageCashRegisters() {
        while (true) {
            view.print("--- Manage Cash Registers ---");

            List<CashReg> registers = store.getCashRegisters();
            for (CashReg reg : registers) {
                view.print(reg.toString());
            }

            view.print("Choose a register number (1-6) to assign a cashier, or 0 to return:");
            int choice = Integer.parseInt(view.getInput("Your choice: "));

            if (choice == 0) break;

            if (choice < 1 || choice > 6) {
                view.print("Invalid register number!");
                continue;
            }

            List<Cashier> cashiers = store.getAllCashiers();
            if (cashiers.isEmpty()) {
                view.print("No cashiers available. Add some first.");
                continue;
            }

            view.print("Available Cashiers:");
            for (Cashier c : cashiers) {
                view.print("ID: " + c.getId() + " | Name: " + c.getName());
            }

            String cashierId = view.getInput("Enter cashier ID to assign to register or 0 to remove: " );
            if (cashierId.equals("0")) {
                store.getCashRegisters().get(choice - 1).setCashier(null);
                view.print("Cashier removed from register " + choice);
                continue;
            }
            Cashier selected = cashiers.stream()
                    .filter(c -> c.getId().equals(cashierId))
                    .findFirst()
                    .orElse(null);

            if (selected == null) {
                view.print("Cashier not found!");
            } else {
                store.getCashRegisters().get(choice - 1).setCashier(selected);
                view.print("Cashier assigned successfully.");
            }
        }
    }
}
