package service;

import model.Cashier;
import model.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CashierServiceTest {

    private Store store;
    private CashierService cashierService;
    private final String TEST_FILE = "src/test_cashiers.txt";

    @BeforeEach
    public void setup() {
        store = new Store();
        store.getAllCashiers().clear(); // Изчистваме списъка с касиери в паметта
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete(); // Изтриваме файла, ако вече съществува
        cashierService = new CashierService(store, TEST_FILE);
    }

    @Test
    public void testCreateCashier() {
        Cashier c = cashierService.createCashier("Ivan", 1200.00);
        assertNotNull(c);
        assertEquals("Ivan", c.getName());
        assertEquals(1200.00, c.getSalary());
        assertNotNull(c.getId());
    }

    @Test
    public void testAddAndGetAllCashiers() {
        Cashier c = cashierService.createCashier("Maria", 1400.00);
        cashierService.addCashier(c);

        List<Cashier> result = cashierService.getAllCashiers();
        assertEquals(1, result.size());
        assertEquals("Maria", result.get(0).getName());
    }

    @Test
    public void testRemoveCashierById() {
        Cashier c = cashierService.createCashier("Nikolay", 1300.00);
        cashierService.addCashier(c);
        boolean removed = cashierService.removeCashierById(c.getId());
        assertTrue(removed);
        assertTrue(cashierService.getAllCashiers().isEmpty());
    }
}
