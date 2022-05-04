package Acceptance.Tests;
import static org.junit.jupiter.api.Assertions.*;

import Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;
@DisplayName("Member Buy Tests  - AT")
public class MemberBuyTests extends MarketTests{
    String uuid;
    @BeforeEach
    public void setUp() {
        uuid = market.guestVisit();
    }

    @AfterEach
    public void tearDown() {
        market.resetMemory(); // discard all resources(cart,members,history purchases...)
        roleBackAfterReset();
        market.exitSystem(uuid);
    }

    /**
     * Requirement: Logout  - #2.3.1
     */
    @Test
    @DisplayName("req: #2.3.1 - success test")
    void Logout_Success() {
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ItemDetail item = new ItemDetail("iphone6",  1, 60, List.of("phone"), "phone");
        item.itemID = IPHONE_6_ID;
        assertTrue(market.addToCart(uuid, existing_storeID, item));

        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        assertTrue(market.guestOnline(uuid));
        assertTrue(market.cartExists(uuid));
        memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        ATResponseObj<List<List<ItemDetail>>> response = market.getCart(uuid);
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
        assertTrue(market.register(uuid, user.username, user.password));
        assertTrue(market.isMember(user));
        ATResponseObj<String> memberID = market.login(uuid, user);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        //TODO: this doesnt check with params of discount policy/but type at this point of version..
        ATResponseObj<Integer> response = market.addStore(uuid, user);
        assertFalse(response.errorOccurred());
        int storeID = response.value;
        System.out.println(storeID);
        assertTrue(market.isContributor(storeID,user));

    }

    @Test
    @DisplayName("req: #2.3.2 - fail test [guest trying to add store]")
    void createStore_Fail1() {
        User user = generateUser();
        ATResponseObj<Integer> response = market.addStore(uuid, user);
        assertTrue(response.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.3.2 - fail test [invalid input]")
    void createStore_Fail2() {
        ATResponseObj<Integer> response = market.addStore(uuid, null);
        assertTrue(response.errorOccurred());
    }


}
