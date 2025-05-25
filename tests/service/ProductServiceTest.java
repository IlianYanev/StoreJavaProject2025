package service;

import model.Product;
import model.ProductCategory;
import model.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.StoreView;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    private Store store;
    ProductService productService = new ProductService(store, "src/test_products.txt");
    private static final String TEST_FILE = "src/test_products.txt";

    @BeforeEach
    public void setup() {
        store = new Store();
        StoreView view = new StoreView();
        StoreService storeService = new StoreService(store, view) {
            @Override
            public void saveProductsToFile() {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
                    oos.writeObject(store.getAllProducts());
                } catch (Exception e) {
                    fail("Could not write test file");
                }
            }

            @Override
            public void loadProductsFromFile() {
                File file = new File(TEST_FILE);
                if (!file.exists()) return;
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    List<Product> products = (List<Product>) ois.readObject();
                    store.getAllProducts().clear();
                    store.getAllProducts().addAll(products);
                } catch (Exception e) {
                    fail("Could not read test file");
                }
            }
        };

        productService = new ProductService(store, "src/test_products.txt") {
            @Override
            public void addProduct(Product product) {
                store.addProduct(product);
                storeService.saveProductsToFile();
            }

            @Override
            public List<Product> getAllProducts() {
                storeService.loadProductsFromFile();
                return store.getAllProducts();
            }

            @Override
            public boolean removeProductById(String id) {
                storeService.loadProductsFromFile();
                boolean removed = store.removeProductById(id);
                if (removed) {
                    storeService.saveProductsToFile();
                }
                return removed;
            }
        };
    }

    @Test
    public void testCreateProduct() {
        Product p = productService.createProduct("Apple", 1.2, ProductCategory.FOOD, LocalDate.now().plusDays(5), 10);
        assertEquals("Apple", p.getName());
        assertEquals(1.2, p.getPurchasePrice());
        assertEquals(ProductCategory.FOOD, p.getCategory());
    }

    @Test
    public void testAddAndGetAllProducts() {
        Product p = productService.createProduct("Bread", 1.0, ProductCategory.FOOD, LocalDate.now().plusDays(3), 5);
        productService.addProduct(p);

        List<Product> all = productService.getAllProducts();
        assertFalse(all.isEmpty());
        assertEquals("Bread", all.get(0).getName());
    }

    @Test
    public void testRemoveProductById() {
        Product p = productService.createProduct("Cheese", 2.5, ProductCategory.FOOD, LocalDate.now().plusDays(10), 3);
        productService.addProduct(p);

        String id = p.getId();
        boolean removed = productService.removeProductById(id);

        assertTrue(removed);
    }
}
