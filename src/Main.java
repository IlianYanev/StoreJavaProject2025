import controller.StoreController;
import model.Store;
import view.StoreView;

public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        StoreView view = new StoreView();        // изглед – отговаря за вход/изход от конзолата
        StoreController controller = new StoreController(store, view);  // контролер – логиката на приложението
        controller.start();                      // стартираме приложението
    }
}
