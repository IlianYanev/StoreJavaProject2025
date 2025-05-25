package service;

import model.Cashier;
import model.Store;

import java.util.List;

public class CashierService {
    private final Store store;
    private final String cashierFilePath;

    public CashierService(Store store) {
        this(store, "src/cashiers.txt");
    }

    public CashierService(Store store, String path) {
        this.store = store;
        this.cashierFilePath = path;
    }

    public Cashier createCashier(String name, double salary) {
        String id = store.generateNextCashierId();
        return new Cashier(id, name, salary);
    }

    public void addCashier(Cashier cashier) {
        store.getAllCashiers().add(cashier);
        store.saveCashiersToFile(cashierFilePath);
    }

    public List<Cashier> getAllCashiers() {
        store.loadCashiersFromFile(cashierFilePath);
        return store.getAllCashiers();
    }

    public boolean removeCashierById(String id) {
        boolean removed = store.getAllCashiers().removeIf(c -> c.getId().equals(id));
        if (removed) {
            store.saveCashiersToFile(cashierFilePath);
        }
        return removed;
    }
}
