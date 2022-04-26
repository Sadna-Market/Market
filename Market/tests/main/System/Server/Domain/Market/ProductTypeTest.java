package main.System.Server.Domain.Market;

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
    ProductType pt=new ProductType(1,"Abba Ganov","90210");


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
    @Test
    void storeExist() {
        for (int i=0; i<10; i++) {
            assertTrue(pt.storeExist(i));
        }
    }

    @DisplayName("storeID is exist -  failure")
    @Test
    void storeExist2() {
        for (int i=10; i<20; i++) {
            assertFalse(pt.storeExist(i));
        }
    }

    @DisplayName("storeID is exist invalid arg -  successful")
    @Test
    void storeExist3() {
        assertFalse(pt.storeExist(-1));
        assertFalse(pt.storeExist(-59));
    }

    @DisplayName("getStores -  successful")
    @Test
    void getStores() {
        List<Integer> stores= pt.getStores();
        pt.addStore(11);
        assertNotEquals(stores,pt.getStores());
    }

    @DisplayName("add store  -  successful")
    @Test
    void addStore() {
        for (int i=50; i<60;i++)
            assertTrue(pt.addStore(i));
    }

    @DisplayName("add store  -  failure")
    @Test
    void addStore2() {
        for (int i=0; i<9;i++)
            assertFalse(pt.addStore(i));
    }


    @DisplayName("containName  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"Abba","a","Ganov","Abba Ganov"})
    @Test
    void containName(String name) {
        assertTrue(pt.containName(name));
    }

    @DisplayName("containName  -  failure")
    @ParameterizedTest
    @ValueSource(strings = {"abba","B","ganov","abba ganov"})
    @Test
    void containName2(String name) {
        assertFalse(pt.containName(name));
    }

    @DisplayName("containDesc  -  successful")
    @ParameterizedTest
    @ValueSource(strings = {"0","90","","90210"})
    @Test
    void containDesc(String name) {
        assertTrue(pt.containDesc(name));
    }

    @DisplayName("containName  -  failure")
    @ParameterizedTest
    @ValueSource(strings = {"8","01209","0000","11"," "})
    @Test
    void containDesc2(String name) {
        assertFalse(pt.containDesc(name));
    }

    @DisplayName("SetRate  -  successful")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,10})
    @Test
    void rate(int i) {
        assertTrue(pt.setRate(i));
    }
}