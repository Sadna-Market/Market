package main.System.Server.Domain.Market;

import main.System.Server.Domain.StoreModel.DiscountPolicy;
import main.System.Server.Domain.StoreModel.Store;
import main.System.Server.Domain.UserModel.UserManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarketTest {
    static Logger logger=Logger.getLogger(MarketTest.class);
    Market market=new Market(new UserManager());


    @BeforeEach
    void setUP() {
        logger.info("new test will run right now");
        market.setForTesting();
    }

    @AfterEach
    void tearDown(){
        market = new Market(new UserManager());
        logger.info("the test finished  to run right now");
    }

    @DisplayName("getInfoProductInStore  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,9})
    @Test
    void getStoreInfo(int i) {
        market.addNewProductToStore(1,i,1,0.5,100);
        assertNotNull(market.getInfoProductInStore(i,1));
    }

    @DisplayName("getStore  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,9})
    @Test
    void getStore(int i) {
        assertEquals(i,market.getStore(i).getRate());
    }

    @DisplayName("getStore  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,10,15,100})
    @Test
    void getStore2(int i) {
        assertNull(market.getStore(i));
    }

    @DisplayName("searchProductByName  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"1","2","3","4"})
    @Test
    void searchProductByName(String s) {
        assertEquals(1,market.searchProductByName(s).size());
    }

    @DisplayName("searchProductByName  -  failure")
    @ParameterizedTest
    @ValueSource(strings = {"yaki","10","store","  "})
    @Test
    void searchProductByName2(String s) {
        assertEquals(0,market.searchProductByName(s).size());
    }

    @DisplayName("searchProductByDesc  -  successful")
    @Test
    void searchProductByDesc2() {
        assertEquals(10,market.searchProductByName("hello").size());
    }

    @DisplayName("searchProductByName  -  failure")
    @ParameterizedTest
    @ValueSource(strings = {"Hello","H","hELLO","  "})
    @Test
    void searchProductByDesc(String s) {
        assertEquals(0,market.searchProductByDesc(s).size());
    }

    @DisplayName("searchProductByRate  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,0})
    @Test
    void searchProductByRate(int i) {
        assertEquals(10-i,market.searchProductByRate(i).size());
    }

    @DisplayName("searchProductByRate  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-3,-100,11,1000,15})
    @Test
    void searchProductByRate2(int i) {
        assertNull(market.searchProductByRate(i));
    }

    @DisplayName("searchProductByStoreRate  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,0})
    @Test
    void searchProductByStoreRate(int i) {
        assertEquals(10-i,market.searchProductByStoreRate(i).size());
    }

    @DisplayName("searchProductByStoreRate  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-100,15,11})
    @Test
    void searchProductByStoreRate2(int i) {
        assertNull(market.searchProductByStoreRate(i));
    }

    @DisplayName("searchProductByStoreRate  -  successful")
    @Test
    void searchProductByRangePrices() {
        assertNotNull(market.searchProductByRangePrices(1,0,100));
    }

    @DisplayName("searchProductByStoreRate  -  failure")
    @Test
    void searchProductByRangePrices2() {
        assertNotNull(market.searchProductByRangePrices(1,101,150));
    }


    @DisplayName("getProductType  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {0,2,4,6,8})
    @Test
    void getProductType(int i) {
        assertEquals(i,market.getProductType(i).getRate());
    }

    @DisplayName("getProductType  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-2,25,44,600,48})
    @Test
    void getProductType2(int i) {
        assertNull(market.getProductType(i));
    }

    @DisplayName("searchProductByCategory  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,0})
    @Test
    void searchProductByCategory(int i) {
        assertEquals(i==0? 4:3,market.searchProductByCategory(i).size());
    }
    @DisplayName("searchProductByCategory  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {4,5,6,-1})
    @Test
    void searchProductByCategory2(int i) {
        assertEquals(0,market.searchProductByCategory(i).size());
    }

    //this test is not for UnitTest -> the market here is gateway and just check valid
    //we will check that on integration tests
    @Test
    void addProductToShoppingBag() {
    }


    @DisplayName("openNewStore  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    @Test
    void openNewStore(int i) {
        assertTrue(market.OpenNewStore(i,null,null,new DiscountPolicy(),null,null));
    }

    @DisplayName("openNewStore  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-555,0})
    @Test
    void openNewStore2(int i) {
        assertFalse(market.OpenNewStore(i,null,null,new DiscountPolicy(),null,null));
    }

    @DisplayName("addNewProductToStore  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {20,6293,185})
    @Test
    void addNewProductToStore2(int i) {
        assertTrue(market.addNewProductToStore(i,1,1,0.5,15));
    }

    @DisplayName("addNewProductToStore  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-20,-6293,-185,0})
    @Test
    void addNewProductToStore(int i) {
        assertFalse(market.addNewProductToStore(i,1,1,0.5,15));
    }

    @DisplayName("deleteProductFromStore  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,7,9,4})
    @Test
    void deleteProductFromStore(int i) {
        assertTrue(market.deleteProductFromStore(i,1,1));
    }

    @DisplayName("deleteProductFromStore  -  failure-UserID")
    @ParameterizedTest
    @ValueSource(ints = {-2,-7,-9,-4})
    @Test
    void deleteProductFromStore2(int i) {
        assertFalse(market.deleteProductFromStore(i,1,1));
    }


    @DisplayName("deleteProductFromStore  -  failure-storeID")
    @ParameterizedTest
    @ValueSource(ints = {-2,-7,-9,-4})
    @Test
    void deleteProductFromStore3(int i) {
        assertFalse(market.deleteProductFromStore(1,i,1));
    }

    @DisplayName("deleteProductFromStore  -  failure-productID")
    @ParameterizedTest
    @ValueSource(ints = {-2,-7,-9,-4})
    @Test
    void deleteProductFromStore4(int i) {
        assertFalse(market.deleteProductFromStore(1,1,i));
    }


    @DisplayName("setProductInStore  -  successful-storeID")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,0})
    @Test
    void setProductInStore(int i) {
        assertTrue(market.setProductInStore(1,i,1,"","",15,15,""));
    }

    @DisplayName("setProductInStore  -  failure-productID")
    @ParameterizedTest
    @ValueSource(ints = {200,-300,500,-800})
    @Test
    void setProductInStore2(int i) {
        assertFalse(market.setProductInStore(1,1,i,"","",15,15,""));
    }


    @DisplayName("deleteStore3  - successful -UserID")
    @ParameterizedTest
    @ValueSource(ints = {1,2,4,6})
    @Test
    void deleteStore(int i) {
        assertTrue(market.deleteStore(i,1));
    }

    @DisplayName("deleteStore3  - successful -StoreID")
    @ParameterizedTest
    @ValueSource(ints = {1,2,4,6})
    @Test
    void deleteStore2(int i) {
        assertTrue(market.deleteStore(1,i));
    }

    @DisplayName("deleteStore3  - failure -StoreID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-40,-66666,90})
    @Test
    void deleteStore3(int i) {
        assertFalse(market.deleteStore(1,i));
    }

    @DisplayName("deleteStore3  - failure -UserID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-40,-66666,0})
    @Test
    void deleteStore4(int i) {
        assertFalse(market.deleteStore(i,1));
    }

    @DisplayName("getStoreOrderHistory  - successful -UserID")
    @ParameterizedTest
    @ValueSource(ints = {1,2,4,6})
    @Test
    void getStoreOrderHistory(int i) {
        assertTrue(market.getStoreOrderHistory(i,1));
    }

    @DisplayName("getStoreOrderHistory  - successful -StoreID")
    @ParameterizedTest
    @ValueSource(ints = {1,2,4,6})
    @Test
    void getStoreOrderHistory2(int i) {
        assertTrue(market.getStoreOrderHistory(1,i));
    }

    @DisplayName("getStoreOrderHistory  - failure -StoreID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-40,-66666,90})
    @Test
    void getStoreOrderHistory3(int i) {
        assertFalse(market.getStoreOrderHistory(1,i));
    }

    @DisplayName("getStoreOrderHistory  - failure -UserID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-40,-66666,0})
    @Test
    void getStoreOrderHistory4(int i) {
        assertFalse(market.getStoreOrderHistory(i,1));
    }

}