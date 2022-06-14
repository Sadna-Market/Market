package com.example.Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class ConcurrentPurchaseTest extends MarketTests {
    String uuid;
    Boolean guestSuccessBuy = null;
    Boolean registeredUserSuccessBuy = null;

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
    @DisplayName("purchase at same time last item")
    public void purchase() throws InterruptedException {
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        CreditCard creditCard = new CreditCard("1111222233334444", "1125", "111");
        User registeredUser = generateUser();
        assertTrue(market.register(uuid, registeredUser.username, registeredUser.password,registeredUser.dateOfBirth));
        //pre conditions
        //action1
        ATResponseObj<String> memberID = market.login(uuid, registeredUser);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertTrue(market.addToCart(uuid, existing_storeID, item1));
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        //action2
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
        assertTrue(market.addToCart(uuid, existing_storeID, item1));


        //main action

        Thread guestBuy = new Thread(() -> {
            ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
            guestSuccessBuy = !response.errorOccurred();
        });
        //main action
        Thread memberBuy = new Thread(() -> {
            ATResponseObj<String> id = market.login(market.guestVisit(), registeredUser);
            ATResponseObj<String> response = market.purchaseCart(id.value, creditCard, address);
            registeredUserSuccessBuy = !response.errorOccurred();
        });
        memberBuy.start();
        guestBuy.start();
        guestBuy.join();
        memberBuy.join();
        assertTrue(guestSuccessBuy ^ registeredUserSuccessBuy);
    }
}
