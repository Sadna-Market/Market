package com.example.Unit.StoreModel;

import Stabs.ProductTypeStab;

import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.StoreModel.BuyRules.UserBuyRule;
import com.example.demo.Domain.StoreModel.History;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Domain.StoreModel.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    Store store = new Store(1,"Best Store", null, null, "dor@gmail.com");
    ProductType productType1 = new ProductTypeStab(1, "milk", "good milk",1);
    ProductType productType2 = new ProductTypeStab(2, "table", "good table",1);
    String user = "liel@gmail.com";
    String user2 = "niv@gmail.com";
    String founder = "dor@gmail.com";
    UserBuyRule uRule;
    @BeforeEach
    void setUp() {
        uRule = new UserBuyRule(new UserPred("dor"));
        store = new Store(1,"Best Store", null, null, founder);
        productType1 = new ProductTypeStab(1, "milk", "good milk",1);
        productType2 = new ProductTypeStab(2, "table", "good table",1);
    }

    @AfterEach
    void tearDown() {

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("addNewBuyRule  -  successful")
    void addNewBuyRule() {
        assertTrue(store.addNewBuyRule(uRule).getValue());
        assertEquals(1,store.getBuyRulesSize());
    }

    @Test
    @DisplayName("removeBuyRule  -  successful")
    void removeBuyRule() {
        assertTrue(store.addNewBuyRule(uRule).getValue());
        assertEquals(1,store.getBuyRulesSize());
        assertTrue(store.removeBuyRule(1).getValue());
        assertEquals(0,store.getBuyRulesSize());
    }

    @Test
    @DisplayName("checkBuyRule  -  successful")
    void checkBuyPolicyS() {
        assertTrue(store.addNewProduct(productType1,10,2.0).value);
        ConcurrentHashMap<Integer,Integer> bag = new ConcurrentHashMap<>();
        bag.put(productType1.getProductID().getValue(),3);
        assertTrue(store.addNewBuyRule(uRule).getValue());
        assertEquals(1,store.getBuyRulesSize());
        assertEquals(0.0,store.checkBuyAndDiscountPolicy("niv",20,bag).getValue()); //when discount work cange expected
    }

    @Test
    @DisplayName("checkBuyRule  -  failure")
    void checkBuyPolicyF() {
        assertTrue(store.addNewProduct(productType1,10,2.0).value);
        ConcurrentHashMap<Integer,Integer> bag = new ConcurrentHashMap<>();
        bag.put(productType1.getProductID().getValue(),3);
        assertTrue(store.addNewBuyRule(uRule).getValue());
        assertEquals(1,store.getBuyRulesSize());
        assertTrue(store.checkBuyAndDiscountPolicy("dor",20,bag).errorOccurred());
    }

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

    @DisplayName("createBID  -  success")
    @Test
    void createBIDS() {
        assertEquals(0, store.getBids().size());
        assertTrue(store.addNewProduct(productType1,5,22.0).value);
        assertTrue(store.createBID("dor@gmail.com",productType1.getProductID().value,2,100).value);
        assertEquals(1, store.getBids().size());
        assertTrue(store.createBID("dor@gmail.com",productType1.getProductID().value,2,100).errorOccurred());
        assertEquals(1, store.getBids().size());
    }

    @DisplayName("createBID  -  failure")
    @Test
    void createBIDF() {
        assertEquals(0, store.getBids().size());
        assertTrue(store.addNewProduct(productType1,5,22.0).value);
        assertTrue(store.createBID("dor@gmail.com",productType1.getProductID().value,10,100).errorOccurred());
        assertEquals(0, store.getBids().size());
    }


    @DisplayName("removeBIDS  -  success")
    @Test
    void removeBIDS() {
        assertEquals(0, store.getBids().size());
        addProductAndCreateBID(); // add product to store and create BID
        assertEquals(1, store.getBids().size());
        assertTrue(store.removeBID(user,productType1.getProductID().value).value);
        assertEquals(0, store.getBids().size());
    }

    @DisplayName("removeBIDS  -  failure")
    @Test
    void removeBIDF() {
        assertEquals(0, store.getBids().size());
        addProductAndCreateBID(); // add product to store and create BID
        assertEquals(1, store.getBids().size());
        assertTrue(store.removeBID(user2,productType1.getProductID().value).errorOccurred());
        assertEquals(1, store.getBids().size());
    }

    @DisplayName("allApprovedBID  -  success")
    @Test
    void allApprovedBIDS() {
        addProductAndCreateBID();
        int productID = productType1.getProductID().value;
        store.getBids().getFirst().addManagerToList(user2);
        assertFalse(store.allApprovedBID(user,productID));
        assertTrue(store.approveBID(founder,user,productID).value);
        assertFalse(store.allApprovedBID(user,productID));
        assertTrue(store.approveBID(user2,user,productID).value);
        assertTrue(store.allApprovedBID(user,productID));
        assertEquals("BIDApproved", store.getBIDStatus(user, productID).value);
    }

    @DisplayName("allApprovedBID  -  failure")
    @Test
    void allApprovedBIDF() {
        assertFalse(store.allApprovedBID(user,1));
    }

    @DisplayName("approveBID  -  success")
    @Test
    void approveBIDS() {
        addProductAndCreateBID();
        int productID = productType1.getProductID().value;
        assertEquals("WaitingForApprovals", store.getBIDStatus(user, productID).value);
        assertTrue(store.approveBID(founder,user,productID).value);
        assertTrue(store.allApprovedBID(user,productID));
        assertFalse(store.approveBID(founder,user,productID).value);
        assertEquals("BIDApproved", store.getBIDStatus(user, productID).value);
    }

    @DisplayName("approveBID  -  failure")
    @Test
    void approveBIDF() {
        assertFalse(store.approveBID(founder,user,1).value);
    }

    @DisplayName("rejectBID  -  failure")
    @Test
    void rejectBIDS() {
        addProductAndCreateBID();
        int productID = productType1.getProductID().value;
        assertEquals("WaitingForApprovals", store.getBIDStatus(user, productID).value);
        assertTrue(store.rejectBID(founder,user,productID).value);
        assertEquals("BIDRejected", store.getBIDStatus(user, productID).value);
    }

    @DisplayName("rejectBID  -  failure")
    @Test
    void rejectBIDF() {
        assertFalse(store.rejectBID(founder,user,1).value);
    }


    @DisplayName("counterBID  -  success")
    @Test
    void counterBIDS() {
        addProductAndCreateBID();
        int productID = productType1.getProductID().value;
        assertEquals("WaitingForApprovals", store.getBIDStatus(user, productID).value);
        assertTrue(store.counterBID(founder,user,productID,1111).value);
        assertEquals("CounterBID", store.getBIDStatus(user, productID).value);
    }

    @DisplayName("counterBID  -  failure")
    @Test
    void counterBIDF() {
        assertFalse(store.counterBID(founder,user,1,111).value);
    }

    @DisplayName("newStoreRate  -  success")
    @Test
    void responseCounterBIDS() {
        addProductAndCreateBID();
        int productID = productType1.getProductID().value;
        assertEquals("WaitingForApprovals", store.getBIDStatus(user, productID).value);
        assertTrue(store.counterBID(founder,user,productID,1111).value);
        assertEquals("CounterBID", store.getBIDStatus(user, productID).value);
        assertTrue(store.responseCounterBID(user,productID,true).value);
        assertFalse(store.responseCounterBID(user,productID,true).value);
        assertEquals("WaitingForApprovals", store.getBIDStatus(user, productID).value);
        assertTrue(store.counterBID(founder,user,productID,1111).value);
        assertEquals("CounterBID", store.getBIDStatus(user, productID).value);
        assertTrue(store.responseCounterBID(user,productID,false).value);
        assertEquals("BIDRejected", store.getBIDStatus(user, productID).value);
    }

    @DisplayName("newStoreRate  -  failure")
    @Test
    void responseCounterBIDF() {
        assertFalse(store.responseCounterBID(user,1,true).value);
    }


    @DisplayName("canBuyBID  -  success")
    @Test
    void canBuyBIDS() {
        addProductAndCreateBID();
        int productID = productType1.getProductID().value;
        assertEquals("WaitingForApprovals", store.getBIDStatus(user, productID).value);
        assertTrue(store.approveBID(founder,user,productID).value);
        assertTrue(store.allApprovedBID(user,productID));
        assertEquals("BIDApproved", store.getBIDStatus(user, productID).value);
        assertFalse(store.canBuyBID(user,productID).errorOccurred());
    }

    @DisplayName("canBuyBID  -  failure")
    @Test
    void canBuyBIDF1() {
        assertTrue(store.canBuyBID(user,1).errorOccurred());
    }


    @DisplayName("canBuyBID  -  failure")
    @Test
    void canBuyBIDF2() {
        addProductAndCreateBID();
        int productID = productType1.getProductID().value;
        assertEquals("WaitingForApprovals", store.getBIDStatus(user, productID).value);
        assertTrue(store.approveBID(founder,user,productID).value);
        assertTrue(store.allApprovedBID(user,productID));
        assertEquals("BIDApproved", store.getBIDStatus(user, productID).value);
        assertTrue(store.setProductQuantity(productID,1).value);
        assertTrue(store.canBuyBID(user,productID).errorOccurred());
    }


    private void addProductAndCreateBID(){
        assertTrue(store.addNewProduct(productType1,5,22.0).value);
        assertTrue(store.createBID(user,productType1.getProductID().value,3,100).value);
    }
}



