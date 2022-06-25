package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Service.Facade;
import com.example.demo.Service.ServiceObj.ServiceBuyPolicy;
import com.example.demo.Service.ServiceObj.ServiceBuyStrategy;
import com.example.demo.Service.ServiceObj.ServiceDiscountPolicy;
import com.example.demo.Service.ServiceObj.ServiceStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class StoreServiceTest {

    @Autowired
    private StoreService storeService;
    @Autowired
    ProductStoreService productStoreService;

    @Autowired
    private Facade market;


    @Test
    void insertStore() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        //action
        assertTrue(storeService.insertStore(dataStore));
        //check
        assertNotEquals(0, dataStore.getStoreId());
    }

    @Test
    void openCloseOpen() {
        var uuidres = market.guestVisit();
        assertFalse(uuidres.errorOccurred());
        String uuid = uuidres.value;
        uuid = market.login(uuid, "sysManager@gmail.com", "Shalom123$").value;
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        var res = market.openNewStore(uuid, "moshe", "sysManager@gmail.com", new ServiceDiscountPolicy(), new ServiceBuyPolicy(), new ServiceBuyStrategy());
        assertFalse(res.errorOccurred());

        //action -check
        var res2 = market.closeStore(uuid,res.value);
        assertFalse(res2.errorOccurred());
        dataStore = storeService.getStoreById(res.value);
        assertFalse(dataStore.getOpen());

        res2 = market.reopenStore(uuid,res.value);
        dataStore = storeService.getStoreById(res.value);
        assertTrue(dataStore.getOpen());

        var res3 = market.newStoreRate(uuid,res.value,6);
        assertFalse(res3.errorOccurred());
        dataStore = storeService.getStoreById(res.value);
        assertEquals(6,dataStore.getRate());

    }

    @Test
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
    void updateStore() {
        //pre
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        assertTrue(storeService.insertStore(dataStore));
        //action
        dataStore.setRate(44);
        dataStore.setOpen(false);
        dataStore.setName("moshe");
        assertTrue(storeService.updateStore(dataStore));

        //check
        List<DataStore> afterStores = storeService.getAllStores();
        if (afterStores.size() != 1) {
            afterStores.forEach(System.out::println);
        }
        assertEquals(1, afterStores.size());
        DataStore afterStore = afterStores.get(0);
        assertEquals(44, afterStore.getRate());
        assertEquals(false, afterStore.getOpen());
        assertEquals("moshe", afterStore.getName());
        assertEquals(dataStore.getStoreId(), afterStore.getStoreId());
    }

    @Test
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
        assertEquals(dataStore.getHistory().size(), dataStore1.getHistory().size());
    }

    @Test
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
            assertEquals(dataStore.getHistory().size(), afterStore.getHistory().size());
        }
    }

    @Test
    void openStores() {
        //action
        String uuid = market.guestVisit().value;
        uuid = market.login(uuid, "sysManager@gmail.com", "Shalom123$").value;
        for (int i = 0; i < 5; i++) {
            var name = "myStore" + i;
            var founder = "sysManager@gmail.com";
            market.openNewStore(uuid, name, founder, new ServiceDiscountPolicy(), new ServiceBuyPolicy(), new ServiceBuyStrategy());
        }
        var stores = market.getAllStores().value;

        List<DataStore> afterStores = storeService.getAllStores();
        //check
        assertEquals(5, afterStores.size());
        for (int i = 0; i < 5; i++) {
            ServiceStore dataStore = stores.get(i);
            DataStore afterStore = afterStores.get(i);
            assertEquals(dataStore.getName(), afterStore.getName());
            assertEquals(dataStore.getFounder(), afterStore.getFounder());
            assertEquals(dataStore.isOpen(), afterStore.getOpen());
            assertEquals(dataStore.getRate(), afterStore.getRate());
        }
    }

    @Test
    void addProductStore() {
        //action
        var uuidres = market.guestVisit();
        assertFalse(uuidres.errorOccurred());
        String uuid = uuidres.value;
        uuid = market.login(uuid, "sysManager@gmail.com", "Shalom123$").value;
        int productTypeId = market.addNewProductType(uuid, "banana", "banana", 1).value;
        for (int i = 0; i < 3; i++) {
            var name = "myStore" + i;
            var founder = "sysManager@gmail.com";
            market.openNewStore(uuid, name, founder, new ServiceDiscountPolicy(), new ServiceBuyPolicy(), new ServiceBuyStrategy());
        }
        //
        var res = market.addNewProductToStore(uuid, 1, productTypeId, 10, 100);
        assertFalse(res.errorOccurred());
        var p = productStoreService.getProductStoreByStoreId(1);
        assertEquals("banana", p.get(0).getProductType().getProductName());

    }

    @Test
    void removeAndUpdateProductStore() {
        //action
        String uuid = market.guestVisit().value;
        uuid = market.login(uuid, "sysManager@gmail.com", "Shalom123$").value;
        int productTypeId1 = market.addNewProductType(uuid, "banana1", "banana", 1).value;
        int productTypeId2 = market.addNewProductType(uuid, "banana2", "banana", 1).value;
        int productTypeId3 = market.addNewProductType(uuid, "banana3", "banana", 1).value;
        int productTypeId4 = market.addNewProductType(uuid, "banana4", "banana", 1).value;
        for (int i = 0; i < 3; i++) {
            var name = "myStore" + i;
            var founder = "sysManager@gmail.com";
            market.openNewStore(uuid, name, founder, new ServiceDiscountPolicy(), new ServiceBuyPolicy(), new ServiceBuyStrategy());
        }
        //pre
        var res = market.addNewProductToStore(uuid, 1, productTypeId1, 10, 100);
        assertFalse(res.errorOccurred());
        res = market.addNewProductToStore(uuid, 1, productTypeId2, 10, 100);
        assertFalse(res.errorOccurred());
        res = market.addNewProductToStore(uuid, 2, productTypeId2, 10, 100);
        assertFalse(res.errorOccurred());
        res = market.addNewProductToStore(uuid, 3, productTypeId3, 10, 100);
        assertFalse(res.errorOccurred());
        res = market.addNewProductToStore(uuid, 3, productTypeId4, 10, 100);
        assertFalse(res.errorOccurred());

        //action
        res = market.deleteProductFromStore(uuid, 1, productTypeId1);
        assertFalse(res.errorOccurred());
        var l1 = productStoreService.getProductStoreByStoreId(1);
        var l2 = productStoreService.getProductStoreByStoreId(2);
        var l3 = productStoreService.getProductStoreByStoreId(3);

        //post
        assertEquals(1, l1.size());
        assertEquals(1, l2.size());
        assertEquals(2, l3.size());
        assertEquals("banana2", l1.get(0).getProductType().getProductName());
        assertEquals("banana2", l2.get(0).getProductType().getProductName());
        assertEquals("banana3", l3.get(0).getProductType().getProductName());
        assertEquals("banana4", l3.get(1).getProductType().getProductName());

        //action
        res = market.setProductQuantityInStore(uuid, 1, productTypeId2, 1000);
        assertFalse(res.errorOccurred());
        res = market.setProductPriceInStore(uuid, 1, productTypeId2, 50.5);
        assertFalse(res.errorOccurred());

        l1 = productStoreService.getProductStoreByStoreId(1);
        assertEquals(productTypeId2, l1.get(0).getProductType().getProductTypeId());
        assertEquals(1, l1.size());
        assertEquals(1000, l1.get(0).getQuantity());
        assertEquals(50.5, l1.get(0).getPrice());


    }


}