package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Market.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    Inventory i = new Inventory(1);
    ProductType productType1 = new ProductType(1, "milk", "good milk");
    ProductType productType2 = new ProductType(2, "table", "good table");

    @BeforeEach
    void setUp() {
        Store store = new Store("Best Store", null, null, "dor@gmail.com");
        ProductType productType1 = new ProductType(1, "milk", "good milk");
        ProductType productType2 = new ProductType(2, "table", "good table");
        i.addNewProduct(productType1,6,5.3);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    @DisplayName("addNewProduct  -  successful")
    void addNewProductS() {
        assertTrue(i.addNewProduct(productType2, 6, 5.3));
    }

    @Test
    @DisplayName("addNewProduct  -  failure")
    void addNewProductF2() {
        assertTrue(i.addNewProduct(productType2, 6, 5.3));
        assertFalse(i.addNewProduct(productType2, 6, 5.3));
    }

    @Test
    @DisplayName("isProductExistInStock  -  success")
    void isProductExistInStockS() {
        assertTrue(i.isProductExistInStock(productType1.getProductID(), 0));
        assertTrue(i.isProductExistInStock(productType1.getProductID(), 2));
        assertTrue(i.isProductExistInStock(productType1.getProductID(), 6));
    }


    @Test
    @DisplayName("isProductExistInStock  -  failure")
    void isProductExistInStockF() {
        assertFalse(i.isProductExistInStock(productType1.getProductID(), 7));
        assertFalse(i.isProductExistInStock(productType1.getProductID(), 100));
    }

    @Test
    @DisplayName("isProductExistInStock  -  failure")
    void isProductExistInStockF2() {
        assertFalse(i.isProductExistInStock(-1, 6));
        assertFalse(i.isProductExistInStock(23, 6));
    }


    @Test
    @DisplayName("removeProduct  -  success")
    void removeProduct() {
        assertTrue(i.removeProduct(productType1.getProductID()));
        assertFalse(i.isProductExistInStock(productType1.getProductID(), 0));
    }


    @Test
    @DisplayName("removeProduct  -  failure")
    void removeProduct1() {
        assertFalse(i.removeProduct(productType2.getProductID()));
    }

    @Test
    @DisplayName("removeProduct  -  failure")
    void removeProduct2() {
        assertFalse(i.removeProduct(productType1.getProductID()-1));
    }



    @DisplayName("setProductQuantity  -  successful")
    @Test
    void setProductQuantityS() {
        assertTrue(i.setProductQuantity(productType1.getProductID(), 33));
        ProductStore ps = i.getProduct(productType1.getProductID());
        assertEquals(33, ps.getQuantity());
    }

    @DisplayName("setProductQuantity  -  failure")
    @Test
    void setProductQuantityF() {
        assertFalse(i.setProductQuantity(productType1.getProductID() + 1, 33));
        ProductStore ps = i.getProduct(productType1.getProductID());
        assertEquals(6, ps.getQuantity());
    }

    @DisplayName("setProductPrice  -  successful")
    @Test
    void setProductPriceS() {
        assertTrue(i.setProductPrice(productType1.getProductID(), 155.3));
        assertEquals(155.3, i.getPrice(productType1.getProductID()));
    }

    @DisplayName("setProductPrice  -  failure")
    @Test
    void setProductPriceF() {
        assertFalse(i.setProductPrice(productType1.getProductID() + 1, 155.3));
        assertEquals(5.3, i.getPrice(productType1.getProductID()));
    }


    @Test
    void getProduct() {
        assertSame(null , i.getProduct(productType2.getProductID()));
        assertSame(productType1.getProductName() , i.getProduct(productType1.getProductID()).getProductType().getProductName());
    }
}