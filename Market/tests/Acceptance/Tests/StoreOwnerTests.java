package Acceptance.Tests;
import static org.junit.jupiter.api.Assertions.*;

import Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;
@DisplayName("Store Owner Tests  - AT")
public class StoreOwnerTests extends MarketTests{
    @BeforeEach
    public void setUp() {
        market.initSystem();
    }

    @AfterEach
    public void tearDown() {
        market.resetMemory(); // discard all resources(cart,members,history purchases...)
        market.exitSystem();
        initStoreAndItem(); // restore state as before
    }

    /**
     * Requirement: add a product  - #2.4.1.1
     */
    @Test
    @DisplayName("req: #2.4.1.1 - success test")
    void addProduct_Success() {
        ItemDetail item = new ItemDetail("galaxyS10", 8888, 1, 10, List.of("phone"), "phone");
        assertTrue(market.isMember(member));
        assertTrue(market.login(member)); //member is contributor

        assertTrue(market.addItemToStore(existing_storeID, item));

        assertTrue(market.hasItem(existing_storeID, item.itemID));

    }

    @Test
    @DisplayName("req: #2.4.1.1 - fail test [storeID doesnt exist]")
    void addProduct_Fail1() {
        ItemDetail item = new ItemDetail("galaxyS10", 8888, 1, 10, List.of("phone"), "phone");
        assertTrue(market.isMember(member));
        assertTrue(market.login(member)); //member is contributor

        assertFalse(market.addItemToStore(existing_storeID + 70, item));

        assertFalse(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.1 - fail test [product exists already]")
    void addProduct_Fail2() {
        ItemDetail item = new ItemDetail("iphone6", 3000, 1, 60, List.of("phone"), "phone");
        assertTrue(market.isMember(member));
        assertTrue(market.login(member)); //member is contributor

        assertFalse(market.addItemToStore(existing_storeID, item));

        assertTrue(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.1 - fail test [invalid input]]")
    void addProduct_Fail3() {
        assertTrue(market.isMember(member));
        assertTrue(market.login(member)); //member is contributor
        assertFalse(market.addItemToStore(existing_storeID, null));
    }

    @Test
    @DisplayName("req: #2.4.1.1 - fail test [invalid input- price]]")
    void addProduct_Fail4() {
        ItemDetail item = new ItemDetail("galaxyS10", 8888, 1, -10, List.of("phone"), "phone");
        assertTrue(market.isMember(member));
        assertTrue(market.login(member)); //member is contributor
        assertFalse(market.addItemToStore(existing_storeID, item));
        assertFalse(market.hasItem(existing_storeID, item.itemID));
    }

    /**
     * Requirement: remove product from store  - #2.4.1.2
     */
    @Test
    @DisplayName("req: #2.4.1.2 - success test")
    void removeProduct_Success() {
        ItemDetail item = new ItemDetail("iphone6", 3000, 1, 60, List.of("phone"), "phone");
        assertTrue(market.isMember(member));
        assertTrue(market.login(member)); //member is contributor

        assertTrue(market.removeProductFromStore(existing_storeID, item));

        assertFalse(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.2 - fail test [storeID doesnt exist]")
    void removeProduct_Fail1() {
        ItemDetail item = new ItemDetail("iphone6", 3000, 1, 60, List.of("phone"), "phone");
        assertTrue(market.isMember(member));
        assertTrue(market.login(member)); //member is contributor

        assertFalse(market.removeProductFromStore(existing_storeID + 70, item));

        assertTrue(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.2 - fail test [product doesnt exist in store]")
    void removeProduct_Fail2() {
        ItemDetail item = new ItemDetail("galaxyS10", 8888, 1, 10, List.of("phone"), "phone");
        assertTrue(market.isMember(member));
        assertTrue(market.login(member)); //member is contributor

        assertFalse(market.removeProductFromStore(existing_storeID, item));

        assertFalse(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.2 - fail test [invalid input]]")
    void removeProduct_Fail3() {
        assertTrue(market.isMember(member));
        assertTrue(market.login(member)); //member is contributor
        assertFalse(market.removeProductFromStore(existing_storeID, null));
    }

}
