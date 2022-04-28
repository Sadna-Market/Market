package Acceptance.Tests;
import static org.junit.jupiter.api.Assertions.*;

import Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;
@DisplayName("Member Buy Tests  - AT")
public class MemberBuyTests extends MarketTests{
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
     * Requirement: Logout  - #2.3.1
     */
    @Test
    @DisplayName("req: #2.3.1 - success test")
    void Logout_Success() {
        assertTrue(market.isMember(member));
        assertTrue(market.login(member));
        ItemDetail item = new ItemDetail("iphone6", 3000, 1, 60, List.of("phone"), "phone");
        assertTrue(market.addToCart(existing_storeID, item));

        assertTrue(market.logout());

        assertTrue(market.guestOnline());
        assertTrue(market.cartExists());
        assertTrue(market.login(member));
        ATResponseObj<List<List<ItemDetail>>> response = market.getCart();
        assertFalse(response.errorOccurred());
        List<List<ItemDetail>> cart = response.value;
        assertFalse(cart.isEmpty());
        List<ItemDetail> bag = cart.get(0);
        assertEquals(item.itemID, bag.get(0).itemID);
    }

    /**
     * Requirement: open/create a store  - #2.3.2
     */
    @Test
    @DisplayName("req: #2.3.2 - success test")
    void createStore_Success() {
        User user = generateUser();
        assertTrue(market.register(user.username,user.password));
        assertTrue(market.isMember(user));
        assertTrue(market.login(user));
        //TODO: this doesnt check with params of discount policy/but type at this point of version..
        ATResponseObj<Integer> response = market.addStore(user);
        assertFalse(response.errorOccurred());
        int storeID = response.value;
        assertTrue(market.isContributor(storeID,user));

    }

    @Test
    @DisplayName("req: #2.3.2 - fail test [guest trying to add store]")
    void createStore_Fail1() {
        User user = generateUser();
        ATResponseObj<Integer> response = market.addStore(user);
        assertTrue(response.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.3.2 - fail test [invalid input]")
    void createStore_Fail2() {
        ATResponseObj<Integer> response = market.addStore(null);
        assertTrue(response.errorOccurred());
    }


}
