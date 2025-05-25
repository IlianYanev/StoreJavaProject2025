package model;

import java.io.Serializable;

public class CashReg implements Serializable {
    private final int number;
    private Cashier cashier;

    public CashReg(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    @Override
    public String toString() {
        return "Cash Register " + number + ": " +
                (cashier != null ? cashier.getName() + " (ID: " + cashier.getId() + ")" : "Not Working!");
    }
}