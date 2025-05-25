
package service;

import model.Cashier;
import model.Product;
import model.ProductCategory;
import model.Store;
import org.junit.jupiter.api.*;
import view.StoreView;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReceiptServiceTest {

    private static final String TEST_RECEIPT_FOLDER = "src/test_receipts/";
    private ReceiptService receiptService;
    private Store store;
    private StoreService storeService;

    @BeforeEach
    public void setup() {
        store = new Store();
        storeService = new StoreService(store, new StoreView());
        receiptService = new ReceiptService(TEST_RECEIPT_FOLDER);
        File dir = new File(TEST_RECEIPT_FOLDER);
        if (!dir.exists()) dir.mkdirs();
        for (File file : dir.listFiles()) {
            file.delete();
        }
    }

    @Test
    @Order(1)
    public void testPrintAndSaveReceipt() {
        List<Product> cart = new ArrayList<>();
        cart.add(new Product("1", "Test Product", 5.0, ProductCategory.NON_FOOD, LocalDate.now().plusDays(10), 2));
        Cashier cashier = new Cashier("1", "Cashier One", 1000);

        receiptService.printAndSaveReceipt(cart, cashier, 15.0, 10.0);

        File folder = new File(TEST_RECEIPT_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.matches("receipt_\\d+\\.txt"));
        assertNotNull(files);
        assertEquals(1, files.length);
    }

    @Test
    @Order(2)
    public void testGenerateStoreReport() {
        List<Product> cart = new ArrayList<>();
        Product p = new Product("1", "Report Product", 3.0, ProductCategory.FOOD, LocalDate.now().plusDays(5), 1);
        cart.add(p);
        store.getAllProducts().add(p);
        Cashier cashier = new Cashier("2", "Cashier Report", 100);
        store.getAllCashiers().add(cashier);

        receiptService.printAndSaveReceipt(cart, cashier, 5.0, 3.3); // with markup
        receiptService.generateStoreReport(store, storeService);
    }

}
