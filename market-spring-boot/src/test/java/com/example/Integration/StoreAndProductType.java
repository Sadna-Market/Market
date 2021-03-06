package com.example.Integration;

import Stabs.ProductTypeStab;

import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.StoreModel.History;
import com.example.demo.Domain.StoreModel.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreAndProductType {

    Store store = new Store(1,"Best Store", null, null, "dor@gmail.com");
    ProductType productType1 = new ProductType(1, "milk", "good milk",1);
    ProductType productType2 = new ProductType(2, "table", "good table",1);
    String user = "dor@gmail.com";

    @BeforeEach
    void setUp() {
        store = new Store(1,"Best Store", null, null, "dor@gmail.com");
        productType1 = new ProductType(1, "milk", "good milk",1);
        productType2 = new ProductType(2, "table", "good table",1);
    }

    @AfterEach
    void tearDown() {

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Test
    @DisplayName("addNewProduct  -  successful")
    void addNewProductS() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
    }

    @Test
    @DisplayName("addNewProduct  -  failure")
    void addNewProductF1() {
        assertFalse(store.addNewProduct(null, 6, 5.3).getValue());
    }

    @Test
    @DisplayName("addNewProduct  -  failure")
    void addNewProductF2() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertFalse(store.addNewProduct(productType1, 6, 5.3).getValue());
    }

    @Test
    @DisplayName("isProductExistInStock  -  success")
    void isProductExistInStockS() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertTrue(store.isProductExistInStock(productType1.getProductID().getValue(), 0).getValue());
        assertTrue(store.isProductExistInStock(productType1.getProductID().getValue(), 2).getValue());
        assertTrue(store.isProductExistInStock(productType1.getProductID().getValue(), 6).getValue());
    }


    @Test
    @DisplayName("isProductExistInStock  -  failure")
    void isProductExistInStockF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertFalse(store.isProductExistInStock(productType1.getProductID().getValue(), 7).getValue());
        assertFalse(store.isProductExistInStock(productType1.getProductID().getValue(), 100).getValue());
    }

    @Test
    @DisplayName("isProductExistInStock  -  failure")
    void isProductExistInStockF2() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertFalse(store.isProductExistInStock(-1, 6).getValue());
        assertFalse(store.isProductExistInStock(23, 6).getValue());
    }


    @Test
    @DisplayName("removeProduct  -  success")
    void removeProduct() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertTrue(store.removeProduct(productType1.getProductID().getValue()).getValue());
        assertFalse(store.isProductExistInStock(productType1.getProductID().getValue(), 0).getValue());
    }


    @Test
    @DisplayName("removeProduct  -  failure")
    void removeProduct1() {
        assertFalse(store.removeProduct(productType1.getProductID().getValue()).getValue());
    }

    @Test
    @DisplayName("removeProduct  -  failure")
    void removeProduct2() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertFalse(store.removeProduct(productType1.getProductID().getValue() + 1).getValue());
    }


    @DisplayName("setProductPrice  -  successful")
    @Test
    void setProductPriceS() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertTrue(store.setProductPrice(productType1.getProductID().getValue(), 155.3).getValue());
        assertEquals(155.3, store.getProductPrice(productType1.getProductID().getValue()).getValue());
    }

    @DisplayName("setProductPrice  -  failure")
    @Test
    void setProductPriceF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertFalse(store.setProductPrice(productType1.getProductID().getValue() + 1, 155.3).getValue());
        assertEquals(5.3, store.getProductPrice(productType1.getProductID().getValue()).getValue());
    }

    @DisplayName("setProductQuantity  -  successful")
    @Test
    void setProductQuantityS() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertTrue(store.setProductQuantity(productType1.getProductID().getValue(), 33).getValue());
        assertEquals(33, store.getProductQuantity(productType1.getProductID().getValue()).getValue());
    }

    @DisplayName("setProductQuantity  -  failure")
    @Test
    void setProductQuantityF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertFalse(store.setProductQuantity(productType1.getProductID().getValue() + 1, 33).getValue());
        assertEquals(6, store.getProductQuantity(productType1.getProductID().getValue()).getValue());
    }


    @DisplayName("getStoreProducts  -  success")
    @Test
    void getStoreProductsS() {
        assertEquals(0, store.GetStoreProducts().getValue().size());
    }

    @DisplayName("getStoreProducts  -  success")
    @Test
    void getStoreProductsS2() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertEquals(1, store.GetStoreProducts().getValue().size());
        assertTrue(store.addNewProduct(productType2, 7, 511.3).getValue());
        assertEquals(2, store.GetStoreProducts().getValue().size());
        assertTrue(store.removeProduct(productType1.getProductID().getValue()).getValue());
        assertEquals(1, store.GetStoreProducts().getValue().size());
    }

    @DisplayName("getProductPrice  -  success")
    @Test
    void getProductPrice() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertEquals(5.3, store.getProductPrice(productType1.getProductID().getValue()).getValue());
    }

    @DisplayName("getProductPrice  -  failure")
    @Test
    void getProductPriceF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertNull(store.getProductPrice(productType1.getProductID().getValue() - 1).getValue());
    }

    @DisplayName("getStoreOrderHistory  -  success")
    @Test
    void getStoreOrderHistory() {
        addHistoryS1();
        List<History> h = store.getStoreOrderHistory().getValue();
        assertEquals(1,h.size());
        assertEquals(user, h.get(0).getUser());
    }

    @DisplayName("getStoreOrderHistory  -  success")
    @Test
    void getStoreOrderHistory2() {
        List<History> h = store.getStoreOrderHistory().getValue();
        assertEquals(0,h.size());
    }


    @DisplayName("getUserHistory  -  success")
    @Test
    void getUserHistory() {
        addHistoryS1();
        List<History> h = store.getUserHistory(user).getValue();
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
        List<History> h = store.getUserHistory("d").getValue();
        assertEquals(0, h.size());

    }

    @DisplayName("getTIDHistory  -  success")
    @Test
    void getTIDHistoryS() {
        addHistoryS1();
        List<Integer> h = store.getTIDHistory().getValue();
        assertEquals(1, h.size());
        assertEquals(1,  h.get(0));
    }

    @DisplayName("addHistory  -  success")
    @Test
    void addHistoryS1() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertTrue(store.addNewProduct(productType2, 8, 5.3).getValue());
        ConcurrentHashMap<Integer,Integer> h = new ConcurrentHashMap<>();
        h.put(productType1.getProductID().getValue(),4);
        h.put(productType2.getProductID().getValue(),4);
        History history = (store.addHistory(1,2,user,h,333.5)).getValue();
        assertEquals(history.getUser(), user);
        assertEquals(history.getFinalPrice(), 333.5);
        assertEquals(history.getTID(),1);
        assertEquals(history.getSupplyID(), 2);

    }

    @DisplayName("addHistory  -  failure")
    @Test
    void addHistoryF() {
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        ConcurrentHashMap<Integer,Integer> h = new ConcurrentHashMap<>();
        h.put(productType1.getProductID().getValue(),4);
        h.put(productType2.getProductID().getValue(),4);
        assertTrue(store.addHistory(1,2,"dor@gmail.com",h,333.5).errorOccurred());
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
        assertTrue(store.addNewProduct(productType1, 6, 5.3).getValue());
        assertTrue(store.addNewProduct(productType2, 8, 5.3).getValue());
        ConcurrentHashMap<Integer,Integer> b = new ConcurrentHashMap<>();
        b.put(1,4);
        b.put(2,3);
        double finalPrice = (store.getProductPrice(1).getValue()*4) + (store.getProductPrice(2).getValue()*3);
        assertEquals(finalPrice,store.calculateBagPrice(b).getValue());
    }

    @DisplayName("closeStore  -  success")
    @Test
    void closeStore() {
        assertTrue(store.isOpen().getValue());
        assertTrue(store.closeStore().getValue());
        assertFalse(store.isOpen().getValue());
    }

    @DisplayName("newStoreRate  -  success")
    @Test
    void newStoreRate() {
        assertEquals(5, store.getRate().getValue());
        assertTrue(store.newStoreRate(10).getValue());
        assertEquals(10, store.getRate().getValue());
        assertTrue(store.newStoreRate(6).getValue());
        assertEquals(8, store.getRate().getValue());
    }

    @DisplayName("newStoreRate  -  failure")
    @Test
    void newStoreRate2() {
        assertEquals(5, store.getRate().getValue());
        store.newStoreRate(10);
        assertEquals(10, store.getRate().getValue());
        store.newStoreRate(-1);
        assertEquals(10, store.getRate().getValue());
        store.newStoreRate(0);
        assertEquals(5, store.getRate().getValue());
    }



}

