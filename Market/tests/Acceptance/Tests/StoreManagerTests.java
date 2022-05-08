package Acceptance.Tests;
import static org.junit.jupiter.api.Assertions.*;

import Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;

@DisplayName("Store Manager Tests  - AT")
public class StoreManagerTests extends MarketTests {
    String uuid;

    @BeforeEach
    public void setUp() {
        initMarketWithSysManagerAndItems();
        registerMemberData();
        populateItemsAndStore();
        uuid = market.guestVisit();
    }

    @AfterEach
    public void tearDown() {
        market = null; //for garbage collector
    }
    /**
     * Requirement: Action of manager permission - get purchase history  - #2.5
     */
    @Test
    @DisplayName("req: #2.5 - success test")
    void managerAction_Success() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.assignNewManager(uuid, existing_storeID, newManager));
        assertTrue(market.isManager(existing_storeID, newManager));

        ATResponseObj<List<String>> historyPurchase = market.getHistoryPurchase(uuid, existing_storeID);
        assertFalse(historyPurchase.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.5 - fail test [no permission]")
    void managerAction_Fail1() {
        User user = generateUser();
        assertTrue(market.register(uuid, user.username, user.password));
        assertTrue(market.isMember(user));
        ATResponseObj<String> memberID = market.login(uuid, user);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        ATResponseObj<List<String>> historyPurchase = market.getHistoryPurchase(uuid, existing_storeID);
        assertTrue(historyPurchase.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.5 - fail test [invalid store id]")
    void managerAction_Fail2() {
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        ATResponseObj<List<String>> historyPurchase = market.getHistoryPurchase(uuid, -1);
        assertTrue(historyPurchase.errorOccurred());
    }
}
