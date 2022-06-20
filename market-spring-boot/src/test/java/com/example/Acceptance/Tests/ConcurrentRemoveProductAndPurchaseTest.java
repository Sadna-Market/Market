package com.example.Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.Acceptance.Obj.*;
import com.example.demo.Service.ServiceObj.ServiceDetailsPurchase;
import org.junit.jupiter.api.*;

import java.util.List;

public class ConcurrentRemoveProductAndPurchaseTest extends MarketTests {
    String uuid;
    Boolean removeSuccess = null;
    Boolean buySuccess = null;

    @BeforeEach
    public void setUp() {
        initMarketWithSysManagerAndItems();
        registerMemberData();
        populateItemsAndStore();
        uuid = market.guestVisit();
    }

    @AfterEach
    public void tearDown() {
        market.resetMemory();
        market = null; //for garbage collector
    }

    @Test
    @DisplayName("remove product from store and buy as same time")
    public void assignSameTime() throws InterruptedException {
        User buyer = generateUser();
        assertTrue(market.register(uuid, buyer.username, buyer.password,buyer.dateOfBirth));
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ATResponseObj<String> buyerID = market.login(uuid, buyer);
        assertFalse(buyerID.errorOccurred());
        uuid = buyerID.value;
        market.addToCart(uuid, existing_storeID, item1);
        uuid = market.logout(uuid).value;

        Thread removeProduct = new Thread(() -> {
            ATResponseObj<String> id = market.login(market.guestVisit(), member);
            removeSuccess = market.removeProductFromStore(id.value, existing_storeID, item1);
        });
        Thread purchaseProduct = new Thread(() -> {
            ATResponseObj<String> id = market.login(market.guestVisit(), buyer);
            ATResponseObj<ServiceDetailsPurchase> res = market.purchaseCart(id.value, new CreditCard("1111222233333334444", "1124", "111"), new Address("Tel-Aviv", "Nordau", 5));
            buySuccess = !res.errorOccurred();
        });
        removeProduct.start();
        purchaseProduct.start();
        removeProduct.join();
        purchaseProduct.join();
        assertTrue(removeSuccess ^ buySuccess);
    }
}
