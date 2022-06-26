package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataBID;
import com.example.demo.DataAccess.Entity.DataBuyRule;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.DataAccess.Enums.Status;
import com.example.demo.Domain.StoreModel.BID;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.BuyRules.*;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Domain.StoreModel.Store;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class BIDServiceTest {

    @Autowired
    private BIDService bidService;


    @Autowired
    private StoreService storeService;


    @Test
    @Transactional
    void insertBID() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);
        ConcurrentHashMap<String, Boolean> approves = new ConcurrentHashMap<>();
        approves.put("niv@gmail.com", false);
        BID bid = new BID(dataStore.getStoreId(),"dor@gmail.com", 1, "arak", 5, 3, approves);
        DataBID dataBID = bid.getDataObject(dataStore);
        assertTrue(bidService.insertBID(dataBID));

        DataBID dataBIDFromDB = bidService.getBIDById(bid.getBIDID(dataStore.getStoreId()));
        assertEquals(5, (int) dataBIDFromDB.getQuantity());
        assertEquals(3, (int) dataBIDFromDB.getTotalPrice());
        assertEquals(3, (int) dataBIDFromDB.getLastPrice());
        assertEquals(1, (int) dataBIDFromDB.getStore().getStoreId());
        assertEquals("dor@gmail.com", dataBIDFromDB.getId().getUsername());
        assertEquals(1, dataBIDFromDB.getId().getProductTypeId());
        assertEquals(false, dataBIDFromDB.getApproves().get("niv@gmail.com"));
        assertEquals(1, dataBIDFromDB.getApproves().size());
        assertEquals(Status.valueOf(BID.StatusEnum.WaitingForApprovals.name()),dataBIDFromDB.getStatus());

    }


    @Test
    @Transactional
    void deleteBuyRule(){
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);
        ConcurrentHashMap<String, Boolean> approves = new ConcurrentHashMap<>();
        approves.put("niv@gmail.com", false);
        BID bid = new BID(dataStore.getStoreId(),"dor@gmail.com", 1, "arak", 5, 3, approves);
        DataBID dataBID = bid.getDataObject(dataStore);
        assertTrue(bidService.insertBID(dataBID));
        assertTrue(bidService.removeBID(dataBID.getId()));

        DataBID nullBuyRule = bidService.getBIDById(dataBID.getId());
        assertNull(nullBuyRule);
    }


    @Test
    @Transactional
    void updateBID() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);
        ConcurrentHashMap<String, Boolean> approves = new ConcurrentHashMap<>();
        approves.put("niv@gmail.com", false);
        BID bid = new BID(dataStore.getStoreId(),"dor@gmail.com", 1, "arak", 5, 3, approves);
        DataBID dataBID = bid.getDataObject(dataStore);
        assertTrue(bidService.insertBID(dataBID));
        bid.setLastPrice(100);
        bid.setStatus(BID.StatusEnum.CounterBID);
        bid.getApproves().replace("niv@gmail.com",true);
        DataBID dataBIDUpdated = bid.getDataObject(dataStore);

        assertTrue(bidService.insertBID(dataBIDUpdated));
        DataBID dataBIDFromDB = bidService.getBIDById(bid.getBIDID(dataStore.getStoreId()));
        assertEquals(5, (int) dataBIDFromDB.getQuantity());
        assertEquals(3, (int) dataBIDFromDB.getTotalPrice());
        assertEquals(100, (int) dataBIDFromDB.getLastPrice());
        assertEquals(1, (int) dataBIDFromDB.getStore().getStoreId());
        assertEquals("dor@gmail.com", dataBIDFromDB.getId().getUsername());
        assertEquals(1, dataBIDFromDB.getId().getProductTypeId());
        assertEquals(true, dataBIDFromDB.getApproves().get("niv@gmail.com"));
        assertEquals(1, dataBIDFromDB.getApproves().size());
        assertEquals(Status.valueOf(BID.StatusEnum.CounterBID.name()),dataBIDFromDB.getStatus());

    }


    @Test
    @Transactional
    void updateBID2() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);
        ConcurrentHashMap<String, Boolean> approves = new ConcurrentHashMap<>();
        approves.put("niv@gmail.com", false);
        BID bid = new BID(dataStore.getStoreId(),"dor@gmail.com", 1, "arak", 5, 3, approves);
        DataBID dataBID = bid.getDataObject(dataStore);
        assertTrue(bidService.insertBID(dataBID));
        bid.approve("niv@gmail.com");
        bid.allApproved();
        DataBID dataBIDFromDB = bidService.getBIDById(bid.getBIDID(dataStore.getStoreId()));
        assertEquals(5, (int) dataBIDFromDB.getQuantity());
        assertEquals(3, (int) dataBIDFromDB.getTotalPrice());
        assertEquals(3, (int) dataBIDFromDB.getLastPrice());
        assertEquals(1, (int) dataBIDFromDB.getStore().getStoreId());
        assertEquals("dor@gmail.com", dataBIDFromDB.getId().getUsername());
        assertEquals(1, dataBIDFromDB.getId().getProductTypeId());
        assertEquals(true, dataBIDFromDB.getApproves().get("niv@gmail.com"));
        assertEquals(1, dataBIDFromDB.getApproves().size());
        assertEquals(Status.valueOf(BID.StatusEnum.BIDApproved.name()),dataBIDFromDB.getStatus());
    }



}