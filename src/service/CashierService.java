package service;

import model.Cashier;
import model.Store;

import java.util.List;

public class CashierService {
    private final Store store;

    public CashierService(Store store) {
        this.store = store;
    }

    public Cashier createCashier(String name, double salary) {
        String id = store.generateNextCashierId();
        return new Cashier(id, name, salary);
    }

    public void addCashier(Cashier cashier) {
        store.getAllCashiers().add(cashier);
        store.saveCashiersToFile("src/cashiers.txt");
    }

    public List<Cashier> getAllCashiers() {
        store.loadCashiersFromFile("src/cashiers.txt");
        return store.getAllCashiers();
    }

    public boolean removeCashierById(String id) {
        boolean removed = store.getAllCashiers().removeIf(c -> c.getId().equals(id));
        if (removed) {
            store.saveCashiersToFile("src/cashiers.txt");
        }
        return removed;
    }
}
