package Acceptance.Tests;
import static org.junit.jupiter.api.Assertions.*;

import Acceptance.Obj.*;
import org.junit.jupiter.api.*;

import java.util.List;
@DisplayName("Store Owner Tests  - AT")
public class StoreOwnerTests extends MarketTests{
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
     * Requirement: add a product  - #2.4.1.1
     */
    @Test
    @DisplayName("req: #2.4.1.1 - success test")
    void addProduct_Success() {
        ItemDetail item = new ItemDetail("galaxyS10", 1, 10, List.of("phone"), "phone");
        item.itemID = GALAXY_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;


        assertTrue(market.addItemToStore(uuid, existing_storeID, item));

        assertTrue(market.hasItem(existing_storeID, item.itemID));

    }

    @Test
    @DisplayName("req: #2.4.1.1 - fail test [storeID doesnt exist]")
    void addProduct_Fail1() {
        ItemDetail item = new ItemDetail("galaxyS10", 1, 10, List.of("phone"), "phone");
        item.itemID = GALAXY_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertFalse(market.addItemToStore(uuid, existing_storeID + 70, item));

        assertFalse(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.1 - fail test [product exists already]")
    void addProduct_Fail2() {
        ItemDetail item = new ItemDetail("iphone6",  1, 60, List.of("phone"), "phone");
        item.itemID = IPHONE_6_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertFalse(market.addItemToStore(uuid, existing_storeID, item));

        assertTrue(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.1 - fail test [invalid input]]")
    void addProduct_Fail3() {
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertFalse(market.addItemToStore(uuid, existing_storeID, null));
    }

    @Test
    @DisplayName("req: #2.4.1.1 - fail test [invalid input- price]]")
    void addProduct_Fail4() {
        ItemDetail item = new ItemDetail("galaxyS10",  1, -10, List.of("phone"), "phone");
        item.itemID = GALAXY_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertFalse(market.addItemToStore(uuid, existing_storeID, item));
        assertFalse(market.hasItem(existing_storeID, item.itemID));
    }

    /**
     * Requirement: remove product from store  - #2.4.1.2
     */
    @Test
    @DisplayName("req: #2.4.1.2 - success test")
    void removeProduct_Success() {
        ItemDetail item = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        item.itemID = IPHONE_6_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertTrue(market.removeProductFromStore(uuid, existing_storeID, item));

        assertFalse(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.2 - fail test [storeID doesnt exist]")
    void removeProduct_Fail1() {
        ItemDetail item = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        item.itemID = IPHONE_6_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertFalse(market.removeProductFromStore(uuid, existing_storeID + 70, item));

        assertTrue(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.2 - fail test [product doesnt exist in store]")
    void removeProduct_Fail2() {
        ItemDetail item = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        item.itemID = 8888;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertFalse(market.removeProductFromStore(uuid, existing_storeID, item));

        assertFalse(market.hasItem(existing_storeID, item.itemID));
    }

    @Test
    @DisplayName("req: #2.4.1.2 - fail test [invalid input]]")
    void removeProduct_Fail3() {
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertFalse(market.removeProductFromStore(uuid, existing_storeID, null));
    }

    /**
     * Requirement: update product info of store  - #2.4.1.3
     */
    @Test
    @DisplayName("req: #2.4.1.3 - success test")
    void updateProductInStore_Success() {
        ItemDetail existingProduct = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        existingProduct.itemID = IPHONE_6_ID;
        ItemDetail updatedProduct = new ItemDetail("iphone6",  3, 150, List.of("phone"), "phone");
        updatedProduct.itemID = IPHONE_6_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertTrue(market.updateProductInStore(uuid, existing_storeID, existingProduct, updatedProduct));

        ATResponseObj<ItemDetail> response = market.getProduct(existing_storeID, updatedProduct.itemID);
        assertFalse(response.errorOccurred());
        assertEquals(150, updatedProduct.price);
        assertEquals(3, updatedProduct.quantity);
    }

    @Test
    @DisplayName("req: #2.4.1.3 - fail test [storeID doesnt exist]")
    void updateProductInStore_Fail1() {
        ItemDetail existingProduct = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        existingProduct.itemID = IPHONE_6_ID;
        ItemDetail updatedProduct = new ItemDetail("iphone6",  3, 150, List.of("phone"), "phone");
        updatedProduct.itemID = IPHONE_6_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertFalse(market.updateProductInStore(uuid, existing_storeID + 70, existingProduct, updatedProduct));

        ATResponseObj<ItemDetail> response = market.getProduct(existing_storeID, updatedProduct.itemID);
        assertFalse(response.errorOccurred());
        assertEquals(60, updatedProduct.price);
        assertEquals(1, updatedProduct.quantity);
    }

    @Test
    @DisplayName("req: #2.4.1.3 - fail test [product doesnt exist in store]")
    void updateProductInStore_Fail2() {
        ItemDetail existingProduct = new ItemDetail("XXX", 5, 60, List.of("phone"), "phone");
        existingProduct.itemID = 3011;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertFalse(market.updateProductInStore(uuid, existing_storeID, existingProduct, existingProduct));

    }

    @Test
    @DisplayName("req: #2.4.1.3 - fail test [invalid price to update]")
    void updateProductInStore_Fail3() {
        ItemDetail existingProduct = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        existingProduct.itemID = IPHONE_6_ID;
        ItemDetail updatedProduct = new ItemDetail("iphone6",  3, -150, List.of("phone"), "phone");
        updatedProduct.itemID = IPHONE_6_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertFalse(market.updateProductInStore(uuid, existing_storeID, existingProduct, updatedProduct));

        ATResponseObj<ItemDetail> response = market.getProduct(existing_storeID, updatedProduct.itemID);
        assertFalse(response.errorOccurred());
        assertEquals(60, updatedProduct.price);
        assertEquals(1, updatedProduct.quantity);
    }

    @Test
    @DisplayName("req: #2.4.1.3 - fail test [invalid input]]")
    void updateProductInStore_Fail4() {
        ItemDetail existingProduct = new ItemDetail("iphone6", 1, 60, List.of("phone"), "phone");
        existingProduct.itemID = IPHONE_6_ID;
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertFalse(market.updateProductInStore(uuid, existing_storeID, existingProduct, null));
        assertFalse(market.updateProductInStore(uuid, existing_storeID, null, null));
    }

    /**
     * //TODO: not in this version
     * Requirement: policies of buying and discounts  - #2.4.2
     */
    @Test
    @DisplayName("req: #2.4.2 - success test")
    void policy_Success() {
        //TODO: not in this version
    }

    @Test
    @DisplayName("req: #2.4.2 - fail test [...]")
    void policy_Fail1() {
        //TODO: not in this version
    }

    @Test
    @DisplayName("req: #2.4.2 - fail test [...]")
    void policy_Fail2() {
        //TODO: not in this version
    }

    @Test
    @DisplayName("req: #2.4.2 - fail test [...]")
    void policy_Fail3() {
        //TODO: not in this version
    }

    /**
     * Requirement: assign store owner - #2.4.4
     */
    @Test
    @DisplayName("req: #2.4.4 - success test")
    void assignStoreOwner_Success() {
        User newOwner = generateUser();
        assertTrue(market.register(uuid, newOwner.username, newOwner.password));
        assertTrue(market.isMember(newOwner));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newOwner));

        assertTrue(market.assignNewOwner(uuid, existing_storeID, newOwner));

        assertTrue(market.isOwner(existing_storeID, newOwner));
        assertTrue(market.isOwner(existing_storeID, member));
    }

    @Test
    @DisplayName("req: #2.4.4 - fail test [new owner is not a member]")
    void assignStoreOwner_Fail1() {
        User newOwner = generateUser();
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newOwner));

        assertFalse(market.assignNewOwner(uuid, existing_storeID, newOwner));

        assertFalse(market.isOwner(existing_storeID, newOwner));
        assertTrue(market.isOwner(existing_storeID, member));
    }

    @Test
    @DisplayName("req: #2.4.4 - fail test [new owner already is owner of store]")
    void assignStoreOwner_Fail2() {
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));


        assertFalse(market.assignNewOwner(uuid, existing_storeID, member));
        assertTrue(market.isOwner(existing_storeID, member));
    }

    @Test
    @DisplayName("req: #2.4.4 - fail test [store id doesnt exist]")
    void assignStoreOwner_Fail3() {
        User newOwner = generateUser();
        assertTrue(market.register(uuid, newOwner.username, newOwner.password));
        assertTrue(market.isMember(newOwner));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newOwner));

        assertFalse(market.assignNewOwner(uuid, existing_storeID + 50, newOwner));

        assertFalse(market.isOwner(existing_storeID, newOwner));
        assertTrue(market.isOwner(existing_storeID, member));
    }

    @Test
    @DisplayName("req: #2.4.4 - fail test [invalid input]")
    void assignStoreOwner_Fail4() {
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));

        assertFalse(market.assignNewOwner(uuid, existing_storeID, null));

        assertTrue(market.isOwner(existing_storeID, member));
    }

    /**
     * Requirement: assign store manager - #2.4.6
     */
    @Test
    @DisplayName("req: #2.4.6 - success test")
    void assignStoreManager_Success() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password));
        assertTrue(market.isMember(newManager));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newManager));
        assertFalse(market.isManager(existing_storeID, newManager));

        assertTrue(market.assignNewManager(uuid, existing_storeID, newManager));

        assertTrue(market.isManager(existing_storeID, newManager));
        assertFalse(market.isOwner(existing_storeID, newManager));
    }

    @Test
    @DisplayName("req: #2.4.6 - fail test [new manager is not a member]")
    void assignStoreManager_Fail1() {
        User newManager = generateUser();
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newManager));
        assertFalse(market.isManager(existing_storeID, newManager));

        assertFalse(market.assignNewManager(uuid, existing_storeID, newManager));

        assertFalse(market.isManager(existing_storeID, newManager));
    }

    @Test
    @DisplayName("req: #2.4.6 - fail test [new manager already is owner of store]")
    void assignStoreManager_Fail2() {
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));

        assertFalse(market.assignNewManager(uuid, existing_storeID, member));

        assertTrue(market.isOwner(existing_storeID, member));
    }

    @Test
    @DisplayName("req: #2.4.6 - fail test [new manager already is manager of store]")
    void assignStoreManager_Fail3() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password));
        assertTrue(market.isMember(newManager));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newManager));
        assertFalse(market.isManager(existing_storeID, newManager));

        assertTrue(market.assignNewManager(uuid, existing_storeID, newManager));
        assertFalse(market.assignNewManager(uuid, existing_storeID, newManager));

        assertTrue(market.isManager(existing_storeID, newManager));
        assertFalse(market.isOwner(existing_storeID, newManager));
    }

    @Test
    @DisplayName("req: #2.4.6 - fail test [store id doesnt exist]")
    void assignStoreManager_Fail4() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password));
        assertTrue(market.isMember(newManager));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newManager));
        assertFalse(market.isManager(existing_storeID, newManager));

        assertTrue(market.assignNewManager(uuid, existing_storeID + 60, newManager));

        assertFalse(market.isManager(existing_storeID, newManager));
        assertFalse(market.isOwner(existing_storeID, newManager));
    }

    @Test
    @DisplayName("req: #2.4.6 - fail test [invalid input]")
    void assignStoreManager_Fail5() {
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.assignNewManager(uuid, existing_storeID, null));
    }


    /**
     * Requirement: change permission of manger - #2.4.7
     */
    @Test
    @DisplayName("req: #2.4.7 - success test")
    void managerPermissionChange_Success() {
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

        assertTrue(market.updatePermission(uuid, Permission.GET_ORDER_HISTORY, false, newManager, existing_storeID));

        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;
        memberID = market.login(uuid, newManager);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        historyPurchase = market.getHistoryPurchase(uuid, existing_storeID);
        assertTrue(historyPurchase.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.7 - fail test [invalid store id]")
    void managerPermissionChange_Fail1() {
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

        assertFalse(market.updatePermission(uuid, Permission.GET_ORDER_HISTORY, false, newManager, -1));
    }

    @Test
    @DisplayName("req: #2.4.7 - fail test [user is not owner of store]")
    void managerPermissionChange_Fail2() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password));
        assertFalse(market.updatePermission(uuid, Permission.GET_ORDER_HISTORY, false, newManager, existing_storeID));
    }

    @Test
    @DisplayName("req: #2.4.7 - fail test [invalid permissions]")
    void managerPermissionChange_Fail3() {
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

        assertFalse(market.updatePermission(uuid, "killTheManager", false, newManager, existing_storeID));

        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;
        memberID = market.login(uuid, newManager);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        historyPurchase = market.getHistoryPurchase(uuid, existing_storeID);
        assertTrue(historyPurchase.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.7 - fail test [invalid input]")
    void managerPermissionChange_Fail4() {
        assertFalse(market.updatePermission(uuid, null, false, member, existing_storeID));
    }


    /**
     * Requirement: close store  - #2.4.9
     */
    @Test
    @DisplayName("req: #2.4.9 - success test")
    void closeStore_Success() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.closeStore(uuid, existing_storeID));
        assertTrue(market.storeIsClosed(existing_storeID));
        ItemDetail item = new ItemDetail("iphoneZX", 1, 10, List.of("phone"), "phone");
        item.itemID = 1122;
        assertFalse(market.addItemToStore(uuid, existing_storeID,item));
    }

    @Test
    @DisplayName("req: #2.4.9 - fail test [find product after closing]")
    void closeStore_Fail1() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.closeStore(uuid, existing_storeID));
        assertTrue(market.storeIsClosed(existing_storeID));
        ATResponseObj<ItemDetail> res = market.getProduct(existing_storeID, 5000);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.9 - fail test [invalid store ID]")
    void closeStore_Fail2() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertFalse(market.closeStore(uuid, existing_storeID + 3));
    }

    @Test
    @DisplayName("req: #2.4.9 - fail test [user doesnt have permission]")
    void closeStore_Fail3() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertTrue(market.assignNewManager(uuid, existing_storeID, newManager));

        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;
        memberID = market.login(uuid, newManager);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertFalse(market.closeStore(uuid, existing_storeID));
    }

    @Test
    @DisplayName("req: #2.4.9 - fail test [invalid input]")
    void closeStore_Fail4() {
        assertFalse(market.closeStore(uuid, -1));
    }



    /**
     * Requirement: get roles info of a store  - #2.4.11
     */
    @Test
    @DisplayName("req: #2.4.11 - success test")
    void getRoleInfo_Success() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<String> res = market.getUserRoleInfo(uuid, existing_storeID);
        assertFalse(res.errorOccurred());
        assertNotNull(res.value);
        assertNotEquals("", res.value);
    }

    @Test
    @DisplayName("req: #2.4.11 - fail test [store doesnt exist]")
    void getRoleInfo_Fail1() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<String> res = market.getUserRoleInfo(uuid, existing_storeID + 50);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.11 - fail test [invalid permission]")
    void getRoleInfo_Fail2() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertTrue(market.assignNewManager(uuid, existing_storeID, newManager));

        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;
        memberID = market.login(uuid, newManager);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<String> res = market.getStoreInfo(existing_storeID);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.11 - fail test [user doesnt exist]")
    void getRoleInfo_Fail3() {
        ATResponseObj<String> res = market.getStoreInfo(existing_storeID);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.11 - fail test [invalid input]")
    void getRoleInfo_Fail4() {
        ATResponseObj<String> res = market.getStoreInfo(-1);
        assertTrue(res.errorOccurred());
    }

    /**
     * Requirement: get purchase history of store  - #2.4.13
     */
    @Test
    @DisplayName("req: #2.4.13 - success test")
    void purchaseHistory_Success() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID,member));
        ATResponseObj<List<String>> res = market.getHistoryPurchase(uuid, existing_storeID);
        assertFalse(res.errorOccurred());
        assertNotEquals(null,res.value);
    }

    @Test
    @DisplayName("req: #2.4.13 - fail test [store doesnt exist]")
    void purchaseHistory_Fail1() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID,member));
        ATResponseObj<List<String>> res = market.getHistoryPurchase(uuid, existing_storeID+50);
        assertTrue(res.errorOccurred());
    }
    @Test
    @DisplayName("req: #2.4.13 - fail test [no permission]")
    void purchaseHistory_Fail2() {
        User user = generateUser();
        assertTrue(market.register(uuid, user.username, user.password));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<List<String>> res = market.getHistoryPurchase(uuid, existing_storeID);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.13 - fail test [...]")
    void purchaseHistory_Fail3() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID,member));
        ATResponseObj<List<String>> res = market.getHistoryPurchase(uuid, -1);
        assertTrue(res.errorOccurred());
    }


}
