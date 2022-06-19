package com.example.Integration;

import com.example.demo.DataAccess.Services.DataServices;
import com.example.demo.Domain.Market.Market;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.UserManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMangerMarketTest {
    static Logger logger=Logger.getLogger(UserMangerMarketTest.class);
    Market market=new Market(new UserManager(),new DataServices());


    @BeforeEach
    void setUP() {
        logger.info("new test will run right now");
        market.setForIntegrationTestingWithUserManager();
    }


    @AfterEach
    void tearDown(){
        market =new Market(new UserManager(), new DataServices());
        logger.info("the test finished  to run right now");
    }


    @DisplayName("getInfoProductInStore  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,9})
    void getStoreInfo(int i) {
        assertFalse(market.getInfoProductInStore(1,i).errorOccurred());
    }

    @DisplayName("getStore  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,9})
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
    void getStore2(int i) {
        assertTrue(market.getStore(i).errorOccurred());
    }

    @DisplayName("searchProductByName  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"1","2","3","4"})
    void searchProductByName(String s) {
        assertEquals(1,market.searchProductByName(s).value.size());
    }

    @DisplayName("searchProductByName  -  failure")
    @ParameterizedTest
    @ValueSource(strings = {"yaki","10","store","  "})
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
    void searchProductByDesc(String s) {
        assertEquals(0,market.searchProductByDesc(s).getValue().size());
    }

    @DisplayName("searchProductByRate  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,0})
    void searchProductByRate(int i) {
        assertEquals(10-i,market.searchProductByRate(i).getValue().size());
    }

    @DisplayName("searchProductByRate  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-3,-100,11,1000,15})
    void searchProductByRate2(int i) {
        assertTrue(market.searchProductByRate(i).errorOccurred());
    }

    @DisplayName("searchProductByStoreRate  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9})
    void searchProductByStoreRate(int i) {
        assertEquals(11-i,market.searchProductByStoreRate(i).value.size());
    }

    @DisplayName("searchProductByStoreRate  - 0  successful")
    @Test
    void searchProductByStoreRate3() {
        assertEquals(10,market.searchProductByStoreRate(0).value.size());
    }

    @DisplayName("searchProductByStoreRate  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-100,15,11})
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






    @DisplayName("searchProductByCategory  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {4,5,6})
    void searchProductByCategory2(int i) {
        assertEquals(0,market.searchProductByCategory(i).value.size());
    }

    @DisplayName("searchProductByCategory  -  failure negative numbers")
    @ParameterizedTest
    @ValueSource(ints = {-2,-202,-1})
    void searchProductByCategory3(int i) {
        assertTrue(market.searchProductByCategory(i).errorOccurred());
    }

    //this test is not for UnitTest -> the market here is gateway and just check valid
    //we will check that on integration tests
    @Test
    void addProductToShoppingBag() {
    }







    @DisplayName("setProductPriceInStore  -  failure-storeID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-3,-5,70,90,0})
    void setProductPriceInStore2(int i) {
        assertTrue(market.setProductPriceInStore(UUID.randomUUID(), i, 1, 15).errorOccurred());
    }

    @DisplayName("setProductPriceInStore  -  failure-productID")
    @ParameterizedTest
    @ValueSource(ints = {200,-300,500,-800})
    void setProductPriceInStore3(int i) {
        assertTrue(market.setProductPriceInStore(UUID.randomUUID(),1,i,15).errorOccurred());
    }



    @DisplayName("setProductPriceInStore  -  failure-storeID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-3,-5,70,90,0})
    void setProductQuantityInStore3(int i) {
        assertTrue(market.setProductQuantityInStore(UUID.randomUUID(), i, 1, 15).errorOccurred());
    }




    @DisplayName("setProductQuantityInStore  -  failure-productID")
    @ParameterizedTest
    @ValueSource(ints = {200,-300,500,-800})
    void setProductQuantityInStore2(int i) {
        assertTrue(market.setProductQuantityInStore(UUID.randomUUID(),1,i,15).errorOccurred());
    }



    @DisplayName("deleteStore3  - failure -StoreID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-40,-66666,90})
    void deleteStore3(int i) {
        assertTrue(market.closeStore(UUID.randomUUID(),i).errorOccurred());
    }

    @DisplayName("deleteStore3  - failure -UserID")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-40,-66666})
    void deleteStore4(int i) {
        assertTrue(market.closeStore(UUID.randomUUID(),i).errorOccurred());
    }



}
