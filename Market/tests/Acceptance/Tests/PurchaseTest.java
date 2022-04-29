package Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;
import Acceptance.Obj.*;
import org.junit.jupiter.api.*;
import java.util.List;
@DisplayName("Purchase Cart Tests  - AT")
public class PurchaseTest extends MarketTests {
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
     * Requirement: purchase cart  - #2.2.5
     */
    @Test
    @DisplayName("req: #2.2.5 - success test")
    void purchaseCart_Success() {
        //pre conditions
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        ItemDetail item2 = new ItemDetail("screenFULLHD", 3232, 1, 10, List.of("TV"), "screen");
        assertTrue(market.addToCart(existing_storeID,item1));
        assertTrue(market.addToCart(existing_storeID,item2));

        CreditCard creditCard = new CreditCard("1111222233334444","1123","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        ATResponseObj<String> response = market.purchaseCart(creditCard,address);
        assertFalse(response.errorOccurred());
        String recipe = response.value;

        //post conditions
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID,item2);
        assertEquals(0,amountItem1);
        assertEquals(2,amountItem2);
        assertNotEquals("",recipe);
        assertNotNull(recipe);

        ATResponseObj<List<String>> res = market.getHistoryPurchase(existing_storeID);
        List<String> recipes = res.value;
        assertTrue(recipes.contains(recipe));
    }

    @Test
    @DisplayName("req: #2.2.5 - fail test [empty cart to purchase]")
    void purchaseCart_Fail1() {
        //pre conditions
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        ItemDetail item2 = new ItemDetail("screenFULLHD", 3232, 1, 10, List.of("TV"), "screen");
        CreditCard creditCard = new CreditCard("1111222233334444","1123","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        int numOfRecipes = market.getHistoryPurchase(existing_storeID).value.size();

        ATResponseObj<String> response = market.purchaseCart(creditCard,address);
        assertTrue(response.errorOccurred());


        //post conditions
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID,item2);
        assertEquals(1,amountItem1);
        assertEquals(3,amountItem2);

        ATResponseObj<List<String>> res = market.getHistoryPurchase(existing_storeID);
        List<String> recipes = res.value;
        assertEquals(numOfRecipes,recipes.size());
    }

    @Test
    @DisplayName("req: #2.2.5 - fail test [invalid credit card]")
    void purchaseCart_Fail2() {
        //pre conditions
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        ItemDetail item2 = new ItemDetail("screenFULLHD", 3232, 1, 10, List.of("TV"), "screen");
        CreditCard creditCard = new CreditCard("XXX","1113","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        assertTrue(market.addToCart(existing_storeID,item1));
        assertTrue(market.addToCart(existing_storeID,item2));
        int numOfRecipes = market.getHistoryPurchase(existing_storeID).value.size();

        ATResponseObj<String> response = market.purchaseCart(creditCard,address);
        assertTrue(response.errorOccurred());

        //post conditions
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID,item2);
        assertEquals(1,amountItem1);
        assertEquals(3,amountItem2);

        ATResponseObj<List<String>> res = market.getHistoryPurchase(existing_storeID);
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

        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        CreditCard creditCard = new CreditCard("1111222233334444","1125","111");
        //pre conditions
        //action1
        assertTrue(market.login(member));
        assertTrue(market.addToCart(existing_storeID,item1));
        assertTrue(market.logout());
        //action2
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        assertTrue(market.addToCart(existing_storeID,item1));
        int numOfRecipes = market.getHistoryPurchase(existing_storeID).value.size();
        //main action
        ATResponseObj<String> response = market.purchaseCart(creditCard,address);
        assertFalse(response.errorOccurred());
        String recipe = response.value;
        //main expected fail action
        assertTrue(market.login(member));
        response = market.purchaseCart(creditCard,member.addr);
        assertTrue(response.errorOccurred());
        market.logout();

        //post conditions
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        assertEquals(0,amountItem1);

        ATResponseObj<List<String>> res = market.getHistoryPurchase(existing_storeID);
        List<String> recipes = res.value;
        assertEquals(numOfRecipes,recipes.size());
        assertTrue(recipes.contains(recipe));


    }

    @Test
    @DisplayName("req: #2.2.5 - fail test [invalid input]")
    void purchaseCart_Fail4() {
        //pre conditions
        assertTrue(market.cartExists());
        assertTrue(market.guestOnline());
        ItemDetail item1 = new ItemDetail("iphone5", 5000, 1, 10, List.of("phone"), "phone");
        ItemDetail item2 = new ItemDetail("screenFULLHD", 3232, 1, 10, List.of("TV"), "screen");
        CreditCard creditCard = new CreditCard("XXX","1113","111");
        Address address = new Address("Tel-Aviv","Nordau 3",3);
        assertTrue(market.addToCart(existing_storeID,null));
        assertTrue(market.addToCart(existing_storeID,null));
        int numOfRecipes = market.getHistoryPurchase(existing_storeID).value.size();

        ATResponseObj<String> response = market.purchaseCart(creditCard,address);
        assertTrue(response.errorOccurred());

        //post conditions
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID,item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID,item2);
        assertEquals(1,amountItem1);
        assertEquals(3,amountItem2);

        ATResponseObj<List<String>> res = market.getHistoryPurchase(existing_storeID);
        List<String> recipes = res.value;
        assertEquals(numOfRecipes,recipes.size());
    }
}
