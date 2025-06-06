package controller;

import model.Store;
import service.*;
import view.StoreView;


public class StoreController {
    private final Store store;
    private final StoreView view;
    private final StoreService storeService;
    private final ProductController productController;
    private final CashierController cashierController;
    private final CustomerController customerController;
    private final ReceiptService receiptService;


    public StoreController(Store store, StoreView view) {
        this.store = store;
        this.view = view;
        this.storeService = new StoreService(store, view);
        this.receiptService = new ReceiptService("src/receipts/");

        storeService.loadProductsFromFile();
        storeService.loadCashiersFromFile();

        ProductService productService = new ProductService(store, "src/products.txt");
        CashierService cashierService = new CashierService(store);
        CustomerService customerService = new CustomerService(productService, storeService, view);

        this.productController = new ProductController(productService, view);
        this.cashierController = new CashierController(cashierService, view);
        this.customerController = new CustomerController(customerService, storeService, view);
    }

    public void start() {
        boolean running = true;

        while (running) {
            view.print("\n+---- Welcome to the Store ----+");
            view.print("|           1. Owner           |");
            view.print("|           2. Client          |");
            view.print("|           0. Exit            |");
            view.print("+------------------------------+");

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

        storeService.saveProductsToFile();
        storeService.saveCashiersToFile();
    }

    private void ownerMenu() {
        boolean back = false;

        while (!back) {
            view.print("\n+-------- Owner Menu --------+");
            view.print("| 1. Add Product             |");
            view.print("| 2. View Products           |");
            view.print("| 3. Remove Product          |");
            view.print("| 4. Add Cashier             |");
            view.print("| 5. View Cashiers           |");
            view.print("| 6. Remove Cashier          |");
            view.print("| 7. Manage Cash Registers   |");
            view.print("| 8. View All Receipts       |");
            view.print("| 9. View Financial Report   |");
            view.print("| 0. Back                    |");
            view.print("+----------------------------+");


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
                    CashRegService cashRegService = new CashRegService(store);
                    new CashRegController(cashRegService, view).manageCashRegisters();
                    break;
                case "8":
                    receiptService.viewAllReceipts();
                    break;
                case "9":
                    receiptService.generateStoreReport(store, storeService);
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    view.print("Invalid option.");
            }
        }
    }

    private void clientMenu() {
        boolean back = false;

        while (!back) {
            view.print("\n+-------- Client Menu --------+");
            view.print("| 1. Add Products to Cart     |");
            view.print("| 2. View Cart                |");
            view.print("| 3. Checkout                 |");
            view.print("| 0. Back                     |");
            view.print("+-----------------------------+");


            String input = view.getInput("Choose option: ");

            switch (input) {
                case "1":
                    customerController.start();
                    break;
                case "2":
                    customerController.viewCart();
                    break;
                case "3":
                    customerController.checkout();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    view.print("Invalid option.");
            }
        }
    }



}
