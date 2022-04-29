package Integration;

import Stabs.ProductTypeStab;
import main.System.Server.Domain.Market.ProductType;
import main.System.Server.Domain.StoreModel.Inventory;
import main.System.Server.Domain.StoreModel.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryAndProductType {


    Inventory i = new Inventory(1);
    ProductType productType1 = new ProductType(1, "milk", "good milk");
    ProductType productType2 = new ProductType(2, "table", "good table");

    @BeforeEach
    void setUp() {
        i = new Inventory(1);
        productType1 = new ProductType(1, "milk", "good milk");
        productType2 = new ProductType(2, "table", "good table");
        i.addNewProduct(productType1,6,5.3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("addNewProduct  -  successful")
    void addNewProductS() {
        assertTrue(i.addNewProduct(productType2, 6, 5.3).getValue());
    }

    @Test
    @DisplayName("addNewProduct  -  failure")
    void addNewProductF2() {
        assertTrue(i.addNewProduct(productType2, 6, 5.3).getValue());
        assertFalse(i.addNewProduct(productType2, 6, 5.3).getValue());
    }

    @Test
    @DisplayName("isProductExistInStock  -  success")
    void isProductExistInStockS() {
        assertTrue(i.isProductExistInStock(productType1.getProductID(), 0).getValue());
        assertTrue(i.isProductExistInStock(productType1.getProductID(), 2).getValue());
        assertTrue(i.isProductExistInStock(productType1.getProductID(), 6).getValue());
    }


    @Test
    @DisplayName("isProductExistInStock  -  failure")
    void isProductExistInStockF() {
        assertFalse(i.isProductExistInStock(productType1.getProductID(), 7).getValue());
        assertFalse(i.isProductExistInStock(productType1.getProductID(), 100).getValue());
    }

    @Test
    @DisplayName("isProductExistInStock  -  failure")
    void isProductExistInStockF2() {
        assertFalse(i.isProductExistInStock(-1, 6).getValue());
        assertFalse(i.isProductExistInStock(23, 6).getValue());
    }


    @Test
    @DisplayName("removeProduct  -  success")
    void removeProduct() {
        assertTrue(i.removeProduct(productType1.getProductID()).getValue());
        assertFalse(i.isProductExistInStock(productType1.getProductID(), 0).getValue());
    }


    @Test
    @DisplayName("removeProduct  -  failure")
    void removeProduct1() {
        assertFalse(i.removeProduct(productType2.getProductID()).getValue());
    }

    @Test
    @DisplayName("removeProduct  -  failure")
    void removeProduct2() {
        assertFalse(i.removeProduct(productType1.getProductID()-1).getValue());
    }



    @DisplayName("setProductQuantity  -  successful")
    @Test
    void setProductQuantityS() {
        assertTrue(i.setProductQuantity(productType1.getProductID(), 33).getValue());
        int ps = i.getQuantity(productType1.getProductID()).getValue();
        assertEquals(33, ps);
    }

    @DisplayName("setProductQuantity  -  failure")
    @Test
    void setProductQuantityF() {
        assertFalse(i.setProductQuantity(productType1.getProductID() + 1, 33).getValue());
        int ps = i.getQuantity(productType1.getProductID()).getValue();
        assertEquals(6, ps);
    }

    @DisplayName("setProductPrice  -  successful")
    @Test
    void setProductPriceS() {
        assertTrue(i.setProductPrice(productType1.getProductID(), 155.3).getValue());
        assertEquals(155.3, i.getPrice(productType1.getProductID()).getValue());
    }

    @DisplayName("setProductPrice  -  failure")
    @Test
    void setProductPriceF() {
        assertFalse(i.setProductPrice(productType1.getProductID() + 1, 155.3).getValue());
        assertEquals(5.3, i.getPrice(productType1.getProductID()).getValue());
    }

}
