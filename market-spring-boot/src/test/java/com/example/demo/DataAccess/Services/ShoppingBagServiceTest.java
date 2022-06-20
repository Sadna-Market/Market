package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.CompositeKeys.ShoppingBagId;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.ShoppingBag;
import com.example.demo.Domain.UserModel.User;
import com.example.demo.Domain.UserModel.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ShoppingBagServiceTest {
    @Autowired
    private ShoppingBagService shoppingBagService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;

    @Autowired
    UserManager userManager;

    private User user;
    private Store store;

    @BeforeEach
    void setUp() {
        user = new User(
                "niv@gmail.com",
                "1qaz2wsx#EDC",
                "0505555555",
                LocalDate.of(1993, 8, 18));
        assertTrue(userService.insertUser(user.getDataObject()));
        store = new Store(
                "myStore",
                new DiscountPolicy(),
                new BuyPolicy(),
                "niv@gmail.com");
        var dataStore = store.getDataObject();
        storeService.insertStore(dataStore);
        store.setStoreId(dataStore.getStoreId());
    }

    @Test
    void insertShoppingBag() {
        ShoppingBag shoppingBag = new ShoppingBag(store, user.getEmail().value);
        shoppingBag.addProduct(1, 3);
        shoppingBag.addProduct(2, 4);

        //action
        var dataShoppingBag = shoppingBag.getDataObject();
        assertTrue(shoppingBagService.insertShoppingBag(dataShoppingBag));
        //check
        var dataShoppingBags = shoppingBagService.getUserShoppingBags(user.getEmail().value);
        assertFalse(dataShoppingBags.isEmpty());
        assertEquals(3, (int) dataShoppingBags.get(0).getProductQuantity().get(1));
        assertEquals(4, (int) dataShoppingBags.get(0).getProductQuantity().get(2));

    }

    @Test
    void deleteShoppingBag() {
        ShoppingBag shoppingBag = new ShoppingBag(store, user.getEmail().value);
        shoppingBag.addProduct(1, 3);
        shoppingBag.addProduct(2, 4);
        var dataShoppingBag = shoppingBag.getDataObject();
        assertTrue(shoppingBagService.insertShoppingBag(dataShoppingBag));

        //action
        assertTrue(shoppingBagService.deleteShoppingBag(dataShoppingBag.getShoppingBagId()));
        var dataShoppingBags = shoppingBagService.getUserShoppingBags(user.getEmail().value);
        assertTrue(dataShoppingBags.isEmpty());
    }

    @Test
    void updateShoppingBag() {
        ShoppingBag shoppingBag = new ShoppingBag(store, user.getEmail().value);
        var dataShoppingBag = shoppingBag.getDataObject();
        assertTrue(shoppingBagService.insertShoppingBag(dataShoppingBag));

        var dataShoppingBags = shoppingBagService.getUserShoppingBags(user.getEmail().value);
        assertFalse(dataShoppingBags.isEmpty());
        //action
        shoppingBag.addProduct(1, 3);
        shoppingBag.addProduct(2, 4);
        dataShoppingBag = shoppingBag.getDataObject();
        assertTrue(shoppingBagService.updateShoppingBag(dataShoppingBag));
        //check
        dataShoppingBags = shoppingBagService.getUserShoppingBags(user.getEmail().value);
        assertEquals(3, (int) dataShoppingBags.get(0).getProductQuantity().get(1));
        assertEquals(4, (int) dataShoppingBags.get(0).getProductQuantity().get(2));
        //action2
        shoppingBag.removeProductFromShoppingBag(1);
        shoppingBag.setProductQuantity(2, 10);
        dataShoppingBag = shoppingBag.getDataObject();
        assertTrue(shoppingBagService.updateShoppingBag(dataShoppingBag));
        //check2
        dataShoppingBags = shoppingBagService.getUserShoppingBags(user.getEmail().value);
        assertNull(dataShoppingBags.get(0).getProductQuantity().get(1));
        assertEquals(10, (int) dataShoppingBags.get(0).getProductQuantity().get(2));
    }

    @Test
    void addToShoppingBag() {
        user.getShoppingCart().addNewProductToShoppingBag(1, store, 100);
        var bags = shoppingBagService.getUserShoppingBags(user.getEmail().value);
        assertFalse(bags.isEmpty());
        assertEquals(100, bags.get(0).getProductQuantity().get(1));
    }
}