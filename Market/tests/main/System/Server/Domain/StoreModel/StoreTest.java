package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Market.Permission;
import main.System.Server.Domain.Market.ProductType;
import main.System.Server.Domain.UserModel.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    Store store = new Store("Best Store", null, null, "dor@gmail.com");
    ProductType productType1 = new ProductType(1, "milk", "good milk");
    ProductType productType2 = new ProductType(2, "table", "good table");
    String user = "dor@gmail.com";
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        Store store = new Store("Best Store", null, null, "dor@gmail.com");
        ProductType productType1 = new ProductType(1, "milk", "good milk");
        ProductType productType2 = new ProductType(2, "table", "good table");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("addNewProduct  -  successful")
    void addNewProductS() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
    }

    @Test
    @DisplayName("addNewProduct  -  failure")
    void addNewProductF1() {
        assertFalse(store.addNewProduct(null, 6, 5.3));
    }

    @Test
    @DisplayName("addNewProduct  -  failure")
    void addNewProductF2() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertFalse(store.addNewProduct(productType1, 6, 5.3));
    }

    @Test
    @DisplayName("isProductExistInStock  -  success")
    void isProductExistInStockS() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertTrue(store.isProductExistInStock(productType1.getProductID(), 0));
        assertTrue(store.isProductExistInStock(productType1.getProductID(), 2));
        assertTrue(store.isProductExistInStock(productType1.getProductID(), 6));
    }


    @Test
    @DisplayName("isProductExistInStock  -  failure")
    void isProductExistInStockF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertFalse(store.isProductExistInStock(productType1.getProductID(), 7));
        assertFalse(store.isProductExistInStock(productType1.getProductID(), 100));
    }

    @Test
    @DisplayName("isProductExistInStock  -  failure")
    void isProductExistInStockF2() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertFalse(store.isProductExistInStock(-1, 6));
        assertFalse(store.isProductExistInStock(23, 6));
    }


    @Test
    @DisplayName("removeProduct  -  success")
    void removeProduct() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertTrue(store.removeProduct(productType1.getProductID()));
        assertFalse(store.isProductExistInStock(productType1.getProductID(), 0));
    }


    @Test
    @DisplayName("removeProduct  -  failure")
    void removeProduct1() {
        assertFalse(store.removeProduct(productType1.getProductID()));
    }

    @Test
    @DisplayName("removeProduct  -  failure")
    void removeProduct2() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertFalse(store.removeProduct(productType1.getProductID() + 1));
    }

    @DisplayName("getInfoProductInStore  -  successful")
    @Test
    void getProductInStoreInfo() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        ProductStore ps = store.getProductInStoreInfo(productType1.getProductID());
        assertSame(ps.getProductType(), productType1);
        assertEquals(5.3, ps.getPrice());
        assertEquals(6, ps.getQuantity());
    }

    @DisplayName("setProductPrice  -  successful")
    @Test
    void setProductPriceS() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertTrue(store.setProductPrice(productType1.getProductID(), 155.3));
        assertEquals(155.3, store.getProductPrice(productType1.getProductID()));
    }

    @DisplayName("setProductPrice  -  failure")
    @Test
    void setProductPriceF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertFalse(store.setProductPrice(productType1.getProductID() + 1, 155.3));
        assertEquals(5.3, store.getProductPrice(productType1.getProductID()));
    }

    @DisplayName("setProductQuantity  -  successful")
    @Test
    void setProductQuantityS() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertTrue(store.setProductQuantity(productType1.getProductID(), 33));
        ProductStore ps = store.getProductInStoreInfo(productType1.getProductID());
        assertEquals(33, ps.getQuantity());
    }

    @DisplayName("setProductQuantity  -  failure")
    @Test
    void setProductQuantityF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertFalse(store.setProductQuantity(productType1.getProductID() + 1, 33));
        ProductStore ps = store.getProductInStoreInfo(productType1.getProductID());
        assertEquals(6, ps.getQuantity());
    }


    @DisplayName("getStoreProducts  -  success")
    @Test
    void getStoreProductsS() {
        assertEquals(0, store.GetStoreProducts().size());
    }

    @DisplayName("getStoreProducts  -  success")
    @Test
    void getStoreProductsS2() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertEquals(1, store.GetStoreProducts().size());
        assertTrue(store.addNewProduct(productType2, 7, 511.3));
        assertEquals(2, store.GetStoreProducts().size());
        assertTrue(store.removeProduct(productType1.getProductID()));
        assertEquals(1, store.GetStoreProducts().size());
    }

    @DisplayName("getProductPrice  -  success")
    @Test
    void getProductPrice() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertEquals(5.3, store.getProductPrice(productType1.getProductID()));
    }

    @DisplayName("getProductPrice  -  failure")
    @Test
    void getProductPriceF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertNull(store.getProductPrice(productType1.getProductID() - 1));
    }

    @DisplayName("getStoreOrderHistory  -  success")
    @Test
    void getStoreOrderHistory() {
        addHistoryS1();
        List<History> h = store.getStoreOrderHistory();
        assertEquals(1,h.size());
        assertEquals(user, h.get(0).getUser());
    }

    @DisplayName("getStoreOrderHistory  -  success")
    @Test
    void getStoreOrderHistory2() {
        List<History> h = store.getStoreOrderHistory();
        assertEquals(0,h.size());
    }


    @DisplayName("getUserHistory  -  success")
    @Test
    void getUserHistory() {
        addHistoryS1();
        List<History> h = store.getUserHistory(user);
        assertEquals(1, h.size());
        History hu = h.get(0);
        assertEquals(hu.getUser(), user);
        assertEquals(1, hu.getTID());
        assertEquals(333.5, hu.getFinalPrice());
        assertEquals(2, hu.getProducts().size());
    }

    @DisplayName("getUserHistory  -  success")
    @Test
    void getUserHistoryS() {
        addHistoryS1();
        List<History> h = store.getUserHistory("d");
        assertEquals(0, h.size());

    }

    @DisplayName("getTIDHistory  -  success")
    @Test
    void getTIDHistoryS() {
        addHistoryS1();
        List<Integer> h = store.getTIDHistory();
        assertEquals(1, h.size());
        assertEquals(1,  h.get(0));
    }

    @DisplayName("addHistory  -  success")
    @Test
    void addHistoryS1() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertTrue(store.addNewProduct(productType2, 8, 5.3));
        HashMap<Integer,Integer> h = new HashMap<>();
        h.put(productType1.getProductID(),4);
        h.put(productType2.getProductID(),4);
        assertTrue(store.addHistory(1,user,h,333.5));
    }

    @DisplayName("addHistory  -  failure")
    @Test
    void addHistoryF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        HashMap<Integer,Integer> h = new HashMap<>();
        h.put(productType1.getProductID(),4);
        h.put(productType2.getProductID(),4);
        assertFalse(store.addHistory(1,"dor@gmail.com",h,333.5));
    }

    @DisplayName("checkBuyPolicy  -  success")
    @Test
    void checkBuyPolicy() {

    }

    @DisplayName("checkDiscountPolicy  -  success")
    @Test
    void checkDiscountPolicy() {
    }

    @DisplayName("calculateBagPrice  -  success")
    @Test
    void calculateBagPrice() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3));
        assertTrue(store.addNewProduct(productType2, 8, 5.3));
        ConcurrentHashMap<Integer,Integer> b = new ConcurrentHashMap<>();
        b.put(1,4);
        b.put(2,3);
        double finalPrice = (store.getProductPrice(1)*4) + (store.getProductPrice(2)*3);
        assertEquals(finalPrice,store.calculateBagPrice(b));
    }

    @DisplayName("closeStore  -  success")
    @Test
    void closeStore() {
        assertTrue(store.isOpen());
        assertTrue(store.closeStore());
        assertFalse(store.isOpen());
    }

    @DisplayName("newStoreRate  -  success")
    @Test
    void newStoreRate() {
        assertEquals(5, store.getRate());
        assertTrue(store.newStoreRate(10));
        assertEquals(10, store.getRate());
        assertTrue(store.newStoreRate(6));
        assertEquals(8, store.getRate());
    }

    @DisplayName("newStoreRate  -  failure")
    @Test
    void newStoreRate2() {
        assertEquals(5, store.getRate());
        store.newStoreRate(10);
        assertEquals(10, store.getRate());
        store.newStoreRate(-1);
        assertEquals(10, store.getRate());
        store.newStoreRate(0);
        assertEquals(5, store.getRate());
    }

}


