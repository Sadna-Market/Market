package com.example.Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.Acceptance.Obj.*;
import com.example.demo.Service.ServiceObj.ServiceStore;
import org.junit.jupiter.api.*;

import java.util.List;

@DisplayName("System Manager Tests  - AT")
public class SystemManagerTests extends MarketTests{
    String uuid;
    User buyer;
    @BeforeEach
    public void setUp() {
        initMarketWithSysManagerAndItems();
        registerMemberData();
        populateItemsAndStore();
        uuid = market.guestVisit();
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        CreditCard creditCard = new CreditCard("1111222233334444","1123","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        buyer = generateUser();
        market.register(uuid, buyer.username, buyer.password,buyer.dateOfBirth);
        uuid = market.login(uuid, buyer).value;
        market.addToCart(uuid, existing_storeID, item1);
        market.purchaseCart(uuid, creditCard, address);
        uuid = market.logout(uuid).value;
    }

    @AfterEach
    public void tearDown() {
        market.resetMemory();
        market = null; //for garbage collector
    }


    /**
     * Requirement: cancel a member from the market  - #2.6.2
     */
    @Test
    @DisplayName("req: ##2.6.4 - success test")
    void cancelMember_Success() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password,newManager.dateOfBirth));
        ATResponseObj<String> res  = market.login(uuid,member);
        assertFalse(res.errorOccurred());
        uuid = res.value;
        assertTrue(market.assignNewManager(uuid,existing_storeID,newManager));
        assertTrue(market.isManager(existing_storeID,newManager));
        res = market.logout(uuid);
        assertFalse(res.errorOccurred());
        uuid = res.value;

        res = market.login(uuid,sysManager);
        assertFalse(res.errorOccurred());
        uuid = res.value;
        assertTrue(market.cancelMembership(uuid,member));
        ATResponseObj<List<ServiceStore>> stores = market.getAllStores();
        assertFalse(stores.errorOccurred());
        assertTrue(stores.value.isEmpty());
    }

    @Test
    @DisplayName("req: ##2.6.2 - fail test [user doesnt exist in system]")
    void cancelMember_Fail1() {
        User newManager = generateUser();

        ATResponseObj<String> res = market.login(uuid,sysManager);
        assertFalse(res.errorOccurred());
        uuid = res.value;
        assertFalse(market.cancelMembership(uuid,newManager));
    }
    @Test
    @DisplayName("req: ##2.6.2 - fail test [user not system manager]")
    void cancelMember_Fail2() {
        User newManager = generateUser();

        ATResponseObj<String> res = market.login(uuid,member);
        assertFalse(res.errorOccurred());
        uuid = res.value;
        assertFalse(market.cancelMembership(uuid,newManager));
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
        ATResponseObj<List<History>> res2 = market.getHistoryPurchase(uuid, existing_storeID);
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

        ATResponseObj<List<History>> res2 = market.getHistoryPurchase(uuid, existing_storeID+10);
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



    /**
     * Requirement: get info of all members  - #2.6.6
     */
    @Test
    @DisplayName("req: ##2.6.6 - success test")
    void getLoggedInMembers_Success1() {
        ATResponseObj<String> res = market.login(uuid,sysManager);
        assertFalse(res.errorOccurred());
        uuid = res.value;
        ATResponseObj<List<User>> lst = market.getLoggedInMembers(uuid);
        assertFalse(lst.errorOccurred());
        assertFalse(lst.value.isEmpty());
    }
    @Test
    @DisplayName("req: ##2.6.6 - success test")
    void getLoggedOutMembers_Success2() {
        ATResponseObj<String> res = market.login(uuid,sysManager);
        System.out.println("aaadddddddddddddddda");
        assertFalse(res.errorOccurred());
        uuid = res.value;
        ATResponseObj<List<User>> lst = market.getLoggedOutMembers(uuid);
        assertFalse(lst.errorOccurred());
        assertFalse(lst.value.isEmpty());
    }

    @Test
    @DisplayName("req: ##2.6.6 - fail test [no permission]")
    void getMembers_Fail1() {
        ATResponseObj<String> res = market.login(uuid,member);
        assertFalse(res.errorOccurred());
        uuid = res.value;
        ATResponseObj<List<User>> lst = market.getLoggedOutMembers(uuid);
        assertTrue(lst.errorOccurred());
    }

    @Test
    @DisplayName("req: ##2.6.6 - fail test [invalid input]")
    void getMembers_Fail2() {
        ATResponseObj<String> res = market.login(uuid,sysManager);
        assertFalse(res.errorOccurred());
        uuid = res.value;
        ATResponseObj<List<User>> lst = market.getLoggedOutMembers("");
        assertTrue(lst.errorOccurred());
    }


}
