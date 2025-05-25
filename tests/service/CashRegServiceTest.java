package service;

import model.CashReg;
import model.Cashier;
import model.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CashRegServiceTest {

    private Store store;
    private CashRegService cashRegService;

    @BeforeEach
    public void setup() {
        store = new Store();
        cashRegService = new CashRegService(store);
    }

    @Test
    public void testGetCashRegisters() {
        List<CashReg> registers = cashRegService.getCashRegisters();
        assertEquals(6, registers.size());
        assertEquals(1, registers.get(0).getNumber());
    }

    @Test
    public void testAssignCashierToRegister_Success() {
        Cashier cashier = new Cashier("1", "Ivan", 1000);
        store.getAllCashiers().add(cashier);

        boolean result = cashRegService.assignCashierToRegister(1, "1");
        assertTrue(result);

        CashReg reg = store.getCashRegisters().get(0);
        assertNotNull(reg.getCashier());
        assertEquals("Ilian Yanev", reg.getCashier().getName());
    }

    @Test
    public void testAssignCashierToRegister_InvalidCashier() {
        boolean result = cashRegService.assignCashierToRegister(1, "999");
        assertFalse(result);
    }

    @Test
    public void testAssignCashierToRegister_InvalidRegister() {
        Cashier cashier = new Cashier("1", "Petar", 900);
        store.getAllCashiers().add(cashier);

        boolean result = cashRegService.assignCashierToRegister(10, "1"); // няма такава каса
        assertFalse(result);
    }

    @Test
    public void testRemoveCashierFromRegister_Success() {
        Cashier cashier = new Cashier("1", "Maria", 1100);
        store.getAllCashiers().add(cashier);
        store.getCashRegisters().get(2).setCashier(cashier);

        boolean removed = cashRegService.removeCashierFromRegister(3);
        assertTrue(removed);
        assertNull(store.getCashRegisters().get(2).getCashier());
    }

    @Test
    public void testRemoveCashierFromRegister_InvalidRegister() {
        boolean removed = cashRegService.removeCashierFromRegister(999);
        assertFalse(removed);
    }
}
