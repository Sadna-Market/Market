package Acceptance.Tests;

import Acceptance.Obj.ATResponseObj;
import Acceptance.Obj.ItemDetail;
import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Guest Buy Tests  - AT")
public class GuestBuyTests extends MarketTests{
    @BeforeEach
    public void setUp() {
        market.initSystem();
    }

    @AfterEach
    public void tearDown() {
        market.exitSystem();
    }


    /**
     * Requirement: get information of a store and its products  - #2.2.1
     */
    @Test
    @DisplayName("req: #2.2.1 - success test")
    void GetInfoStore_Success() {
        ATResponseObj<String> info = market.getStoreInfo(existing_storeID);
        assertFalse(info.errorOccurred());
        assertNotNull(info.value);
        assertNotSame("", info.value);
    }

    @Test
    @DisplayName("req: #2.2.1 - fail test [storeID not exist]")
    void GetInfoStore_Fail1() {
        ATResponseObj<String> info = market.getStoreInfo(existing_storeID + 5000);
        assertTrue(info.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.2.1 - fail test [invalid input]")
    void GetInfoStore_Fail2() {
        ATResponseObj<String> info = market.getStoreInfo(-1);
        assertTrue(info.errorOccurred());
    }
}
