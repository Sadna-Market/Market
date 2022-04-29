package Acceptance.Tests;
import static org.junit.jupiter.api.Assertions.*;

import Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;

@DisplayName("Store Manager Tests  - AT")
public class StoreManagerTests extends MarketTests {
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
     * Requirement: Action of manager permission - get purchase history  - #2.5
     */
    @Test
    @DisplayName("req: #2.5 - success test")
    void managerAction_Success() {
        User newManager = generateUser();
        assertTrue(market.register(newManager.username,newManager.password));
        assertTrue(market.isMember(member));
        assertTrue(market.login(member));
        assertTrue(market.assignNewManager(existing_storeID, member, newManager));
        assertTrue(market.isManager(existing_storeID, newManager));

        ATResponseObj<List<String>> historyPurchase = market.getHistoryPurchase(existing_storeID);
        assertFalse(historyPurchase.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.5 - fail test [no permission]")
    void managerAction_Fail1() {
        User user = generateUser();
        assertTrue(market.register(user.username,user.password));
        assertTrue(market.isMember(user));
        assertTrue(market.login(user));

        ATResponseObj<List<String>> historyPurchase = market.getHistoryPurchase(existing_storeID);
        assertTrue(historyPurchase.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.5 - fail test [invalid store id]")
    void managerAction_Fail2() {
        assertTrue(market.isMember(member));
        assertTrue(market.login(member));

        ATResponseObj<List<String>> historyPurchase = market.getHistoryPurchase(-1);
        assertFalse(historyPurchase.errorOccurred());
    }
}
