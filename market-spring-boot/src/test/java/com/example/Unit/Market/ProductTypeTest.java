package com.example.Unit.Market;

import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.Response.DResponseObj;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class ProductTypeTest {
    static Logger logger=Logger.getLogger(ProductTypeTest.class);
    ProductType pt=new ProductType(1,"Abba Ganov","90210",1);


    @BeforeEach
    void setUP() {
        logger.info("new test will run right now");
        for (int i=0; i<10; i++)
            pt.addStore(i);
    }

    @AfterEach
    void tearDown(){
        pt.stores=new ArrayList<>();
        logger.info("the test finished  to run right now");
    }



    @DisplayName("storeID is exist -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,9})
    void storeExist(int i) {
        assertFalse(pt.storeExist(i).errorOccurred());
        assertTrue(pt.storeExist(i).getValue());
    }

    @DisplayName("storeID is exist - args invalid  -failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,-3,-5,-7,-9})
    void storeExist2(int i) {
        assertTrue(pt.storeExist(i).errorOccurred());
    }

    @DisplayName("storeID is exist - args invalid  -failure")
    @ParameterizedTest
    @ValueSource(ints = {15,18,55545,295})
    void storeExist3(int i) {
        assertFalse(pt.storeExist(i).getValue());
    }

    @DisplayName("getStores -  successful")
    @Test
    void getStores() {
        DResponseObj<List<Integer>> stores= pt.getStores();
        assertFalse(stores.errorOccurred());
        assertFalse(pt.addStore(11).errorOccurred());
        assertNotEquals(stores.getValue(),pt.getStores().getValue());
    }

    @DisplayName("add store  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {800,10,100,1000})
    void addStore(int i) {
        assertFalse(pt.addStore(i).errorOccurred());
    }

    @DisplayName("add store  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-800,1,2,3,-1})
    void addStore2(int i) {
        assertTrue(pt.addStore(i).errorOccurred());
    }


    @DisplayName("containName  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"Abba","a","Ganov","Abba Ganov"})
    void containName(String name) {
        DResponseObj<Boolean> ans = pt.containName(name);
        assertFalse(ans.errorOccurred());
        if (!ans.errorOccurred())
            assertTrue(ans.getValue());
    }

    @DisplayName("containName  -  failure")
    @ParameterizedTest
    @ValueSource(strings = {"abba","B","ganov","abba ganov"})
    void containName2(String name) {
        DResponseObj<Boolean> ans = pt.containName(name);
        assertFalse(ans.errorOccurred());
    }

    @DisplayName("containDesc  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"0","90","","90210"})
    void containDesc(String name) {
        DResponseObj<Boolean> ans = pt.containDesc(name);
        assertFalse(ans.errorOccurred());
        if (!ans.errorOccurred())
            assertTrue(ans.getValue());
    }

    @DisplayName("containName  -  failure")
    @ParameterizedTest
    @ValueSource(strings = {"8","01209","0000","11"," "})
    void containDesc2(String name) {
        DResponseObj<Boolean> ans = pt.containDesc(name);
        assertFalse(ans.errorOccurred());
    }

    @DisplayName("SetRate  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,10})
    void rate(int i) {
        DResponseObj<Boolean> ans =pt.setRate(i);
        assertFalse(ans.errorOccurred());
        if (!ans.errorOccurred())
            assertTrue(ans.getValue());
    }

    @DisplayName("SetRate  -  failure")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-3,405,505,669,76,846,91,180})
    void rate2(int i) {
        DResponseObj<Boolean> ans =pt.setRate(i);
        assertTrue(ans.errorOccurred());
    }
}