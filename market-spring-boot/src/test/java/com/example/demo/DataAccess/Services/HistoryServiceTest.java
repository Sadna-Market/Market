package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.*;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.StoreModel.*;
import com.example.demo.Domain.UserModel.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class HistoryServiceTest {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private ProductStoreService productStoreService;

//    @Test
////    @Transactional
//    void insertHistory() {
//        User user = new User("niv@gmail.com", "1qaz2wsx3edc$RFV", "0522222222", LocalDate.of(1993, 8, 13));
//        DataUser dataUser = user.getDataObject();
//        assertTrue(userService.insertUser(dataUser));
//
//        ProductType productType = new ProductType("myProduct","blbabla",3);
//        DataProductType dataProductType = productType.getDataObject();
//        assertTrue(productTypeService.insertProductType(dataProductType));
//        productType.setProductID(dataProductType.getProductTypeId());
//
//        Store store = new Store("hanut", new DiscountPolicy(), new BuyPolicy(), "yaki@gmail.com");
//        DataStore dataStore = store.getDataObject();
//        assertTrue(storeService.insertStore(dataStore));
//        store.setStoreId(dataStore.getStoreId());
//
//        ProductStore productStore = new ProductStore(productType,3,4.5);
//        DataProductStore dataProductStore = productStore.getDataObject();
//        assertTrue(productStoreService.insertProductStore(dataProductStore,store.getStoreId().value));
//
//
//        productType.addStore(store.getStoreId().value);
//        var data = productType.getDataObject();
//        assertTrue(productTypeService.updateProductType(data));
//
//        store.setRate(8);
//        store.getInventory().value.addNewProductStore(productType,productStore);
//        var dataStore1 = store.getDataObject();
//        assertTrue(storeService.updateStore(dataStore1));
//
//        History history = new History(22,1,4.5,List.of(productStore),"niv@gmail.com");
//        var dataHistory = history.getDataObject();
//        assertTrue(historyService.insertHistory(dataHistory,store.getStoreId().value,user.getEmail().value));
//
//        //action
//    }
//
//    @Test
//    void getAllHistoryByUsername() {
//    }
//
//    @Test
//    void getAllHistoryByStoreId() {
//    }
}