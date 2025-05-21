package controller;

import model.Store;
import view.StoreView;

public class StoreController {
    private final Store store;
    private final StoreView view;
    private final ProductController productController;
    private final CashierController cashierController;

    public StoreController(Store store, StoreView view) {
        this.store = store;
        this.view = view;
        this.productController = new ProductController(store, view);
        this.cashierController = new CashierController(store, view);
    }

    public void start() {
        boolean running = true;

        while (running) {
            view.print("\n--- Welcome to the Store ---");
            view.print("1. Owner");
            view.print("2. Client");
            view.print("0. Exit");

            String choice = view.getInput("Choose option: ");

            switch (choice) {
                case "1":
                    ownerMenu();
                    break;
                case "2":
                    clientMenu();
                    break;
                case "0":
                    view.print("Goodbye!");
                    running = false;
                    break;
                default:
                    view.print("Invalid option.");
            }
        }
    }

    private void ownerMenu() {
        boolean back = false;

        while (!back) {
            view.print("\n--- Owner Menu ---");
            view.print("1. Add Product");
            view.print("2. View Products");
            view.print("3. Remove Product");
            view.print("4. Add Cashier");
            view.print("5. View Cashiers");
            view.print("6. Remove Cashier");
            view.print("7. Manage Cash Registers");
            view.print("9. Back");

            String choice = view.getInput("Choose option: ");

            switch (choice) {
                case "1":
                    productController.addProduct();
                    break;
                case "2":
                    productController.viewAllProducts();
                    break;
                case "3":
                    productController.removeProduct();
                    break;
                case "4":
                    cashierController.addCashier();
                    break;
                case "5":
                    cashierController.viewCashiers();
                    break;
                case "6":
                    cashierController.removeCashier();
                    break;
                case "7":
                    new CashRegController(store, view).manageCashRegisters();
                    break;
                case "9":
                    back = true;
                    break;
                default:
                    view.print("Invalid option.");
            }
        }
    }

    private void clientMenu() {
        view.print("Client functionality coming soon...");
    }
}
