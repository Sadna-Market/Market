package Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;
import Acceptance.Obj.*;
import org.junit.jupiter.api.*;
import java.util.List;
@DisplayName("Purchase Cart Tests  - AT")
public class PurchaseTest extends MarketTests {
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
        market.resetMemory();
        market = null; //for garbage collector
    }

    /**
     * Requirement: purchase cart  - #2.2.5
     */
    @Test
    @DisplayName("req: #2.2.5 - success test")
    void purchaseCart_Success() {
        //pre conditions
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ItemDetail item2 = new ItemDetail("screenFULLHD", 1, 10, List.of("TV"), "screen");
        item2.itemID = SCREEN_FULL_HD_ID;
        assertTrue(market.addToCart(uuid, existing_storeID, item1));
        assertTrue(market.addToCart(uuid, existing_storeID, item2));

        CreditCard creditCard = new CreditCard("1111222233334444","1123","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertFalse(response.errorOccurred());
        String recipe = response.value;

        //post conditions
        ATResponseObj<String> managerID = market.login(uuid,member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID,item2);
        assertEquals(0,amountItem1);
        assertEquals(2,amountItem2);
        assertNotEquals("",recipe);
        assertNotNull(recipe);

        ATResponseObj<List<String>> res = market.getHistoryPurchase(uuid, existing_storeID);//guest cannot call this func
        List<String> recipes = res.value;
        assertTrue(recipes.contains("1"));
    }

    @Test
    @DisplayName("req: #2.2.5 - fail test [empty cart to purchase]")
    void purchaseCart_Fail1() {
        //pre conditions
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ItemDetail item2 = new ItemDetail("screenFULLHD", 1, 10, List.of("TV"), "screen");
        item2.itemID = SCREEN_FULL_HD_ID;
        CreditCard creditCard = new CreditCard("1111222233334444","1123","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        ATResponseObj<String> memberID = market.login(uuid,member);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        int numOfRecipes = market.getHistoryPurchase(uuid, existing_storeID).value.size();
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertTrue(response.errorOccurred());


        //post conditions
        ATResponseObj<String> managerID = market.login(uuid,member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID,item2);
        assertEquals(1,amountItem1);
        assertEquals(3,amountItem2);

        ATResponseObj<List<String>> res = market.getHistoryPurchase(uuid, existing_storeID);
        List<String> recipes = res.value;
        assertEquals(numOfRecipes,recipes.size());
    }

    @Test
    @DisplayName("req: #2.2.5 - fail test [invalid credit card]")
    void purchaseCart_Fail2() {
        //pre conditions
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ItemDetail item2 = new ItemDetail("screenFULLHD", 1, 10, List.of("TV"), "screen");
        item2.itemID = SCREEN_FULL_HD_ID;
        CreditCard creditCard = new CreditCard("XXX","1113","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        assertTrue(market.addToCart(uuid, existing_storeID, item1));
        assertTrue(market.addToCart(uuid, existing_storeID, item2));
        ATResponseObj<String> memberID = market.login(uuid,member);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        int numOfRecipes = market.getHistoryPurchase(uuid, existing_storeID).value.size();
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertTrue(response.errorOccurred());

        //post conditions
        ATResponseObj<String> managerID = market.login(uuid,member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID,item2);
        assertEquals(1,amountItem1);
        assertEquals(3,amountItem2);


        ATResponseObj<List<String>> res = market.getHistoryPurchase(uuid, existing_storeID);
        List<String> recipes = res.value;
        assertEquals(numOfRecipes,recipes.size());

    }

    @Test
    @DisplayName("req: #2.2.5 - fail test [linear 2 buyers]")
    void purchaseCart_Fail3() {
        //use case:
        //1. Member adds to his cart item1 that the store has only 1 in stock, and logout.
        //2. Guest adds to his cart item1 and then purchases it. (expected success).
        //3. Member login, system will restore his cart. Then member try to purchase (expected fail - item not available).

        ItemDetail item1 = new ItemDetail("iphone5",  1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        CreditCard creditCard = new CreditCard("1111222233334444","1125","111");
        User registeredUser = generateUser();
        assertTrue(market.register(uuid,registeredUser.username,registeredUser.password));
        //pre conditions
        //action1
        ATResponseObj<String> memberID = market.login(uuid, registeredUser);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertTrue(market.addToCart(uuid, existing_storeID, item1));
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        //action2
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        assertTrue(market.addToCart(uuid, existing_storeID, item1));

        //main action
        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertFalse(response.errorOccurred());
        String recipe = response.value;

        //main expected fail action
        memberID = market.login(uuid, registeredUser);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        response = market.purchaseCart(uuid, creditCard, member.addr);
        assertTrue(response.errorOccurred());

        guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        //post conditions
        memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        assertEquals(0,amountItem1);


        ATResponseObj<List<String>> res = market.getHistoryPurchase(uuid, existing_storeID);
        List<String> recipes = res.value;
        assertEquals(1,recipes.size());


    }

    @Test
    @DisplayName("req: #2.2.5 - fail test [invalid input]")
    void purchaseCart_Fail4() {
        //pre conditions
        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ItemDetail item2 = new ItemDetail("screenFULLHD", 1, 10, List.of("TV"), "screen");
        item2.itemID = SCREEN_FULL_HD_ID;

        assertFalse(market.addToCart(uuid, existing_storeID, null));
        assertFalse(market.addToCart(uuid, existing_storeID, null));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        int numOfRecipes = market.getHistoryPurchase(uuid, existing_storeID).value.size();
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        ATResponseObj<String> response = market.purchaseCart(uuid, new CreditCard("xxx","xx","xxx"), new Address("x","ss",-1));
        assertTrue(response.errorOccurred());

        //post conditions
        memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID,item2);
        assertEquals(1,amountItem1);
        assertEquals(3,amountItem2);


        ATResponseObj<List<String>> res = market.getHistoryPurchase(uuid, existing_storeID);
        List<String> recipes = res.value;
        assertEquals(numOfRecipes,recipes.size());
    }
}
