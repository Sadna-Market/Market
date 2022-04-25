package main.System.Server.Domain.Market;

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

    @Test
    void getStoreInfo() {
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

    @DisplayName("getProductType  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,0})
    @Test
    void searchProductByCategory(int i) {
        assertEquals(i==0? 4:3,market.searchProductByCategory(i).size());
    }
    @DisplayName("getProductType  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {4,5,6,-1})
    @Test
    void searchProductByCategory2(int i) {
        assertEquals(0,market.searchProductByCategory(i).size());
    }

    @Test
    void productSearch() {
    }

    @Test
    void addProductToShoppingBag() {
    }

    @Test
    void order() {
    }

    @Test
    void openNewStore() {
    }

    @Test
    void addNewProductToStore() {
    }

    @Test
    void deleteProductFromStore() {
    }

    @Test
    void setProductInStore() {
    }

    @Test
    void addNewStoreOwner() {
    }

    @Test
    void addNewStoreManager() {
    }

    @Test
    void setManagerPermissions() {
    }

    @Test
    void deleteStore() {
    }

    @Test
    void getStoreRoles() {
    }

    @Test
    void getStoreOrderHistory() {
    }
}