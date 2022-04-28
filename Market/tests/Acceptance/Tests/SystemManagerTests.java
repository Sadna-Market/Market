package Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;

import Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;

@DisplayName("System Manager Tests  - AT")
public class SystemManagerTests extends MarketTests{
    User buyer;
    @BeforeEach
    public void setUp() {
        market.initSystem();
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        CreditCard creditCard = new CreditCard("1111222233334444","1123","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        buyer = generateUser();
        market.register(buyer.username, buyer.password);
        market.login(buyer);
        market.addToCart(existing_storeID,item1);
        market.purchaseCart(creditCard,address);
        market.logout();
    }

    @AfterEach
    public void tearDown() {
        market.resetMemory(); // discard all resources(cart,members,history purchases...)
        market.exitSystem();
        initStoreAndItem(); // restore state as before
    }

    /**
     * Requirement: get purchase info of any store or buyer  - #2.6.4
     */
    @Test
    @DisplayName("req: ##2.6.4 - success test")
    void getPurchaseInfoOfStore_Success1() {
        assertTrue(market.login(sysManager));
        ATResponseObj<String> res = market.getStoreInfo(existing_storeID);
        assertFalse(res.errorOccurred());
        assertNotEquals("",res.value);

        ATResponseObj<List<String>> res2 = market.getHistoryPurchase(existing_storeID);
        assertFalse(res2.errorOccurred());
        assertFalse(res2.value.isEmpty());
    }

    @Test
    @DisplayName("req: ##2.6.4 - success test")
    void getPurchaseInfoOfBuyer_Success2() {
        assertTrue(market.login(sysManager));
        ATResponseObj<String> res = market.getBuyerInfo(buyer);
        assertFalse(res.errorOccurred());
        assertNotEquals("",res.value);
    }

    @Test
    @DisplayName("req: ##2.6.4 - fail test [invalid store id]")
    void getPurchaseInfoOfStore_Fail1() {
        assertTrue(market.login(sysManager));
        ATResponseObj<String> res = market.getStoreInfo(existing_storeID+10);
        assertTrue(res.errorOccurred());

        ATResponseObj<List<String>> res2 = market.getHistoryPurchase(existing_storeID+10);
        assertTrue(res2.errorOccurred());
    }
    @Test
    @DisplayName("req: ##2.6.4 - fail test [no permission]")
    void getPurchaseInfoOfBuyer_Fail3() {
        assertTrue(market.login(member));
        ATResponseObj<String> res = market.getBuyerInfo(buyer);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: ##2.6.4 - fail test [user is not member]")
    void getPurchaseInfoOfBuyer_Fail2() {
        assertTrue(market.login(sysManager));
        ATResponseObj<String> res = market.getBuyerInfo(generateUser());
        assertTrue(res.errorOccurred());
    }

}
