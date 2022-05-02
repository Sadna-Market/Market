package main.System.Server.Domain.Market;

import main.System.Server.Domain.Response.DResponseObj;
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
import java.util.UUID;

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
        market =new Market(new UserManager());
        logger.info("the test finished  to run right now");
    }

    @DisplayName("getInfoProductInStore  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,9})
    @Test
    void getStoreInfo(int i) {
        assertFalse(market.getInfoProductInStore(1,i).errorOccurred());
    }

    @DisplayName("getStore  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,9})
    @Test
    void getStore(int i) {
        DResponseObj<Store> store=market.getStore(i);
        int rate=store.getValue().getRate().getValue();
        assertEquals(i,rate);
    }


    @DisplayName("getStore  -  failure for 10")
    @Test
    void getStore3() {
        assertFalse(market.getStore(10).errorOccurred());
    }

    @DisplayName("getStore  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,11,15,100})
    @Test
    void getStore2(int i) {
        assertTrue(market.getStore(i).errorOccurred());
    }

    @DisplayName("searchProductByName  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"1","2","3","4"})
    @Test
    void searchProductByName(String s) {
        assertEquals(1,market.searchProductByName(s).value.size());
    }

    @DisplayName("searchProductByName  -  failure")
    @ParameterizedTest
    @ValueSource(strings = {"yaki","10","store","  "})
    @Test
    void searchProductByName2(String s) {
        assertEquals(0,market.searchProductByName(s).value.size());
    }

    @DisplayName("searchProductByDesc  -  successful")
    @Test
    void searchProductByDesc2() {
        assertEquals(10,market.searchProductByDesc("hello").value.size());
    }

    @DisplayName("searchProductByName  -  failure")
    @ParameterizedTest
    @ValueSource(strings = {"Hello","H","hELLO","  "})
    @Test
    void searchProductByDesc(String s) {
        assertEquals(0,market.searchProductByDesc(s).getValue().size());
    }

    @DisplayName("searchProductByRate  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,0})
    @Test
    void searchProductByRate(int i) {
        assertEquals(10-i,market.searchProductByRate(i).getValue().size());
    }

    @DisplayName("searchProductByRate  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-3,-100,11,1000,15})
    @Test
    void searchProductByRate2(int i) {
        assertTrue(market.searchProductByRate(i).errorOccurred());
    }

    @DisplayName("searchProductByStoreRate  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,0})
    @Test
    void searchProductByStoreRate(int i) {
        assertEquals(10-i,market.searchProductByStoreRate(i).value.size());
    }

    @DisplayName("searchProductByStoreRate  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-100,15,11})
    @Test
    void searchProductByStoreRate2(int i) {
        assertTrue(market.searchProductByStoreRate(i).errorOccurred());
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



    @DisplayName("searchProductByCategory  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,0})
    @Test
    void searchProductByCategory(int i) {
        assertEquals(i==0? 4:3,market.searchProductByCategory(i).value.size());
    }
    @DisplayName("searchProductByCategory  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {4,5,6})
    @Test
    void searchProductByCategory2(int i) {
        assertEquals(0,market.searchProductByCategory(i).value.size());
    }

    @DisplayName("searchProductByCategory  -  failure negative numbers")
    @ParameterizedTest
    @ValueSource(ints = {-2,-202,-1})
    @Test
    void searchProductByCategory3(int i) {
        assertTrue(market.searchProductByCategory(i).errorOccurred());
    }

    //this test is not for UnitTest -> the market here is gateway and just check valid
    //we will check that on integration tests
    @Test
    void addProductToShoppingBag() {
    }



    @DisplayName("setProductPriceInStore  -  successful-productID")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,8})
    @Test
    void setProductPriceInStore4(int i) {
        assertFalse(market.setProductPriceInStore(UUID.randomUUID(),1,i,15).errorOccurred());
    }

    @DisplayName("setProductPriceInStore  -  successful-storeID")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,9})
    @Test
    void setProductPriceInStore(int i) {
        assertFalse(market.setProductPriceInStore(UUID.randomUUID(), i, 1, 15).errorOccurred());
    }

    @DisplayName("setProductPriceInStore  -  failure-storeID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-3,-5,70,90,0})
    @Test
    void setProductPriceInStore2(int i) {
        assertFalse(market.setProductPriceInStore(UUID.randomUUID(), i, 1, 15).errorOccurred());
    }

    @DisplayName("setProductPriceInStore  -  failure-productID")
    @ParameterizedTest
    @ValueSource(ints = {200,-300,500,-800})
    @Test
    void setProductPriceInStore3(int i) {
        assertTrue(market.setProductPriceInStore(UUID.randomUUID(),1,i,15).errorOccurred());
    }

    @DisplayName("setProductQuantityInStore  -  successful-storeID")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,9})
    @Test
    void setProductQuantityInStore(int i) {
        assertFalse(market.setProductQuantityInStore(UUID.randomUUID(),i,1,15).errorOccurred());
    }

    @DisplayName("setProductPriceInStore  -  failure-storeID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-3,-5,70,90,0})
    @Test
    void setProductQuantityInStore3(int i) {
        assertTrue(market.setProductQuantityInStore(UUID.randomUUID(), i, 1, 15).errorOccurred());
    }


    @DisplayName("setProductPriceInStore  -  successful-productID")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,8})
    @Test
    void setProductQuantityInStore4(int i) {
        assertFalse(market.setProductQuantityInStore(UUID.randomUUID(),1,i,15).errorOccurred());
    }


    @DisplayName("setProductQuantityInStore  -  failure-productID")
    @ParameterizedTest
    @ValueSource(ints = {200,-300,500,-800})
    @Test
    void setProductQuantityInStore2(int i) {
        assertTrue(market.setProductQuantityInStore(UUID.randomUUID(),1,i,15).errorOccurred());
    }


    @DisplayName("closeStore  - successful -StoreID")
    @ParameterizedTest
    @ValueSource(ints = {1,2,4,6})
    @Test
    void deleteStore2(int i) {
        assertFalse(market.closeStore(UUID.randomUUID(),i).errorOccurred());
    }

    @DisplayName("deleteStore3  - failure -StoreID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-40,-66666,90})
    @Test
    void deleteStore3(int i) {
        assertTrue(market.closeStore(UUID.randomUUID(),i).errorOccurred());
    }

    @DisplayName("deleteStore3  - failure -UserID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-40,-66666})
    @Test
    void deleteStore4(int i) {
        assertTrue(market.closeStore(UUID.randomUUID(),i).errorOccurred());
    }

    @DisplayName("getStoreOrderHistory  - successful -StoreID")
    @ParameterizedTest
    @ValueSource(ints = {1,2,4,6})
    @Test
    void getStoreOrderHistory2(int i) {
        assertEquals(0,market.getStoreOrderHistory(UUID.randomUUID(),i).value.size());
    }

    @DisplayName("getUserHistoryInStore  - successful")
    @Test
    void getUserHistoryInStore() {
        assertEquals(new ArrayList<>(),market.getUserInfo("123e4567-e89b-12d3-a456-556642440000", "eee").getValue());
    }




}