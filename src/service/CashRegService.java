package service;

import model.CashReg;
import model.Cashier;
import model.Store;

import java.util.List;

public class CashRegService {
    private final Store store;

    public CashRegService(Store store) {
        this.store = store;
    }

    public List<CashReg> getCashRegisters() {
        return store.getCashRegisters();
    }

    public List<Cashier> getAllCashiers() {
        return store.getAllCashiers();
    }

    public boolean assignCashierToRegister(int registerNumber, String cashierId) {
        if (registerNumber < 1 || registerNumber > store.getCashRegisters().size()) {
            return false;
        }

        List<Cashier> cashiers = store.getAllCashiers();
        Cashier selected = cashiers.stream()
                .filter(c -> c.getId().equals(cashierId))
                .findFirst()
                .orElse(null);

        if (selected == null) return false;

        store.getCashRegisters().get(registerNumber - 1).setCashier(selected);
        return true;
    }

    public boolean removeCashierFromRegister(int registerNumber) {
        if (registerNumber < 1 || registerNumber > store.getCashRegisters().size()) {
            return false;
        }

        store.getCashRegisters().get(registerNumber - 1).setCashier(null);
        return true;
    }
}
