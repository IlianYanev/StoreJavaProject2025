package service;

import model.Product;
import model.ProductCategory;
import model.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.StoreView;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {

    private Store store;
    private StoreService storeService;
    private ProductService productService;
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        store = new Store();
        StoreView view = new StoreView();
        storeService = new StoreService(store, view);
        productService = new ProductService(store, "test_products.txt");
        customerService = new CustomerService(productService, storeService, view);
    }

    @Test
    public void testFindProductById_Found() {
        Product product = new Product("1", "Valid", 2.0, ProductCategory.FOOD, LocalDate.now().plusDays(5), 10);
        store.getAllProducts().clear();
        store.getAllProducts().add(product);

        Product found = customerService.findProductById("1");
        assertNotNull(found);
        assertEquals("Valid", found.getName());
    }

    @Test
    public void testFindProductById_NotFound() {
        store.getAllProducts().clear();
        Product found = customerService.findProductById("999");
        assertNull(found);
    }

    @Test
    public void testAddToCart_NotEnoughQuantity() {
        Product product = new Product("1", "Juice", 3.0, ProductCategory.FOOD, LocalDate.now().plusDays(2), 2);
        store.getAllProducts().clear();
        store.getAllProducts().add(product);

        Product selected = customerService.findProductById("1");
        assertThrows(IllegalArgumentException.class, () -> customerService.addToCart(selected, 5));
    }
}
