package Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;

import Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;

@DisplayName("System Manager Tests  - AT")
public class SystemManagerTests extends MarketTests{
    String uuid;
    User buyer;
    @BeforeEach
    public void setUp() {
        uuid = market.initSystem(sysManager).value;
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5;
        CreditCard creditCard = new CreditCard("1111222233334444","1123","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        buyer = generateUser();
        market.register(uuid, buyer.username, buyer.password);
        uuid = market.login(uuid, buyer).value;
        market.addToCart(uuid, existing_storeID, item1);
        market.purchaseCart(uuid, creditCard, address);
        uuid = market.logout(uuid).value;
    }

    @AfterEach
    public void tearDown() {
        market.resetMemory(); // discard all resources(cart,members,history purchases...)
        market.exitSystem(uuid);
        initStoreAndItem(); // restore state as before
    }

    /**
     * Requirement: get purchase info of any store or buyer  - #2.6.4
     */
    @Test
    @DisplayName("req: ##2.6.4 - success test")
    void getPurchaseInfoOfStore_Success1() {
        ATResponseObj<String> memberID = market.login(uuid, sysManager);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<String> res = market.getStoreInfo(existing_storeID);
        assertFalse(res.errorOccurred());
        assertNotEquals("",res.value);

        ATResponseObj<List<String>> res2 = market.getHistoryPurchase(uuid, existing_storeID);
        assertFalse(res2.errorOccurred());
        assertFalse(res2.value.isEmpty());
    }

    @Test
    @DisplayName("req: ##2.6.4 - success test")
    void getPurchaseInfoOfBuyer_Success2() {
        ATResponseObj<String> memberID = market.login(uuid, sysManager);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<String> res = market.getBuyerInfo(uuid, buyer);
        assertFalse(res.errorOccurred());
        assertNotEquals("",res.value);
    }

    @Test
    @DisplayName("req: ##2.6.4 - fail test [invalid store id]")
    void getPurchaseInfoOfStore_Fail1() {
        ATResponseObj<String> memberID = market.login(uuid, sysManager);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<String> res = market.getStoreInfo(existing_storeID+10);
        assertTrue(res.errorOccurred());

        ATResponseObj<List<String>> res2 = market.getHistoryPurchase(uuid, existing_storeID+10);
        assertTrue(res2.errorOccurred());
    }
    @Test
    @DisplayName("req: ##2.6.4 - fail test [no permission]")
    void getPurchaseInfoOfBuyer_Fail3() {
        ATResponseObj<String> memberID = market.login(uuid, member);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<String> res = market.getBuyerInfo(uuid, buyer);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: ##2.6.4 - fail test [user is not member]")
    void getPurchaseInfoOfBuyer_Fail2() {
        ATResponseObj<String> memberID = market.login(uuid, sysManager);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<String> res = market.getBuyerInfo(uuid, generateUser());
        assertTrue(res.errorOccurred());
    }

}
