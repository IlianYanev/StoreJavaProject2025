import controller.StoreController;
import model.Store;
import view.StoreView;

public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        StoreView view = new StoreView();
        StoreController storeController = new StoreController(store, view);
        storeController.start();
    }
}
