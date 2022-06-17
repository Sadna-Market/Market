package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataShoppingCart;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Entity.DataUser;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Test
    @Transactional
    void insertStore() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        //action
        assertTrue(storeService.insertStore(dataStore));
        //check

        assertNotEquals(0, dataStore.getStoreId());
        assertNotEquals(0, dataStore.getInventory().getInventoryId());
        assertNotEquals(0, dataStore.getDiscountPolicy().getDiscountPolicyId());
        assertNotEquals(0, dataStore.getBuyPolicy().getBuyPolicyID());
    }

    @Test
    @Transactional
    void deleteStore() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        assertTrue(storeService.insertStore(dataStore));
        //action
        assertTrue(storeService.deleteStore(dataStore.getStoreId()));
        //check
        DataStore nullStore = storeService.getStoreById(dataStore.getStoreId());
        assertNull(nullStore);
    }

    @Test
    @Transactional
    void updateStore() {
        //pre
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        assertTrue(storeService.insertStore(dataStore));
        //action
        DataStore s2 = store.getDataObject();
        s2.setStoreId(dataStore.getStoreId());
        s2.setRate(44);
        s2.setOpen(false);
        s2.setName("moshe");
        assertTrue(storeService.updateStore(s2));

        //check
        List<DataStore> afterStores = storeService.getAllStores();
        assertEquals(1, afterStores.size());
        DataStore afterStore = afterStores.get(0);
        assertEquals(44, afterStore.getRate());
        assertEquals(false, afterStore.getOpen());
        assertEquals("moshe", afterStore.getName());
        assertEquals(dataStore.getStoreId(), afterStore.getStoreId());
        assertEquals(dataStore.getBuyPolicy().getBuyPolicyID(), afterStore.getBuyPolicy().getBuyPolicyID());
        assertEquals(dataStore.getDiscountPolicy().getDiscountPolicyId(), afterStore.getDiscountPolicy().getDiscountPolicyId());
        assertEquals(dataStore.getInventory().getInventoryId(), afterStore.getInventory().getInventoryId());
    }

    @Test
    @Transactional
    void getStoreById() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        assertTrue(storeService.insertStore(dataStore));
        //action
        DataStore dataStore1 = storeService.getStoreById(dataStore.getStoreId());
        //check
        assertEquals(dataStore.getName(), dataStore1.getName());
        assertEquals(dataStore.getFounder(), dataStore1.getFounder());
        assertEquals(dataStore.getOpen(), dataStore1.getOpen());
        assertEquals(dataStore.getNumOfRated(), dataStore1.getNumOfRated());
        assertEquals(dataStore.getRate(), dataStore1.getRate());
        assertEquals(dataStore.getInventory().getInventoryId(), dataStore1.getInventory().getInventoryId());
        assertEquals(dataStore.getBuyPolicy().getBuyPolicyID(), dataStore1.getBuyPolicy().getBuyPolicyID());
        assertEquals(dataStore.getDiscountPolicy().getDiscountPolicyId(), dataStore1.getDiscountPolicy().getDiscountPolicyId());
        assertEquals(dataStore.getHistory().size(), dataStore1.getHistory().size());
    }

    @Test
    @Transactional
    void getAllSores() {
        Store store1 = new Store("myStore1", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        Store store2 = new Store("myStore2", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        Store store3 = new Store("myStore3", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        Store store4 = new Store("myStore4", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        Store store5 = new Store("myStore5", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        List<DataStore> dataStores = List.of(
                store1.getDataObject(),
                store2.getDataObject(),
                store3.getDataObject(),
                store4.getDataObject(),
                store5.getDataObject());
        dataStores.forEach(dataStore -> {
            assertTrue(storeService.insertStore(dataStore));
        });
        //action
        List<DataStore> afterStores = storeService.getAllStores();
        //check
        assertEquals(5, afterStores.size());
        for (int i = 0; i < 5; i++) {
            DataStore dataStore = dataStores.get(i);
            DataStore afterStore = afterStores.get(i);
            assertEquals(dataStore.getName(), afterStore.getName());
            assertEquals(dataStore.getFounder(), afterStore.getFounder());
            assertEquals(dataStore.getOpen(), afterStore.getOpen());
            assertEquals(dataStore.getNumOfRated(), afterStore.getNumOfRated());
            assertEquals(dataStore.getRate(), afterStore.getRate());
            assertEquals(dataStore.getInventory().getInventoryId(), afterStore.getInventory().getInventoryId());
            assertEquals(dataStore.getBuyPolicy().getBuyPolicyID(), afterStore.getBuyPolicy().getBuyPolicyID());
            assertEquals(dataStore.getDiscountPolicy().getDiscountPolicyId(), afterStore.getDiscountPolicy().getDiscountPolicyId());
            assertEquals(dataStore.getHistory().size(), afterStore.getHistory().size());
        }
    }
}