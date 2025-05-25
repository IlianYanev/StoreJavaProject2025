package service;

import model.*;
import org.junit.jupiter.api.Test;
import view.StoreView;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StoreServiceTest {

    private static final String TEST_CASHIER_FILE = "src/test_cashiers.txt";

    @Test
    public void testSaveAndLoadCashiersToFile() {

        Store store = new Store();
        StoreView view = new StoreView();
        StoreService service = new StoreService(store, view, "src/test_products.txt", TEST_CASHIER_FILE);

        store.getAllCashiers().clear();
        assertEquals(0, store.getAllCashiers().size());


        Cashier testCashier = new Cashier("999", "TestName", 1234.56);
        store.getAllCashiers().add(testCashier);

        service.saveCashiersToFile();

        store.getAllCashiers().clear();
        assertEquals(0, store.getAllCashiers().size());

        service.loadCashiersFromFile();

        List<Cashier> loaded = store.getAllCashiers();
        assertEquals(1, loaded.size());
        assertEquals("TestName", loaded.get(0).getName());

        new File(TEST_CASHIER_FILE).delete();
    }

    @Test
    public void testSaveAndLoadProductsToFile() {
        Store store = new Store();
        StoreView view = new StoreView();
        StoreService service = new StoreService(store, view);

        store.getAllProducts().clear();
        Product testProduct = new Product("888", "TestProduct", 10.0, ProductCategory.NON_FOOD, LocalDate.now().plusDays(10), 5);
        store.getAllProducts().add(testProduct);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/test_products.txt"))) {
            oos.writeObject(store.getAllProducts());
        } catch (IOException e) {
            fail("Exception during saving: " + e.getMessage());
        }

        store.getAllProducts().clear();
        assertEquals(0, store.getAllProducts().size());

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/test_products.txt"))) {
            List<Product> loaded = (List<Product>) ois.readObject();
            store.getAllProducts().addAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            fail("Exception during loading: " + e.getMessage());
        }

        assertEquals(1, store.getAllProducts().size());
        assertEquals("TestProduct", store.getAllProducts().get(0).getName());
    }

    @Test
    public void testGetCashRegisters() {
        Store store = new Store();
        StoreView view = new StoreView();
        StoreService service = new StoreService(store, view);

        List<CashReg> registers = service.getCashRegisters();
        assertNotNull(registers);
        assertEquals(6, registers.size());
    }


}
