package com.example.Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;
import com.example.Acceptance.Obj.*;
import com.example.demo.DataAccess.Repository.HistoryRepository;
import com.example.demo.Domain.StoreModel.BuyRules.AndBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.ProductBuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.ConditionProductDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.SimpleStoreDiscountRule;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.ProductBuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.UserBuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.ConditionProductDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.SimpleStoreDiscountRuleSL;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.UserPredicateSL;
import com.example.demo.Service.ServiceObj.ServiceCreditCard;
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

        CreditCard creditCard = new CreditCard("1111222233334444", "11/23", "111");
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertFalse(response.errorOccurred());
//        String recipe = response.value;

        //post conditions
        ATResponseObj<String> managerID = market.login(uuid, member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID, item2);
        assertEquals(0, amountItem1);
        assertEquals(2, amountItem2);
//        assertNotEquals("",recipe);
//        assertNotNull(recipe);

        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);//guest cannot call this func
        List<History> recipes = res.value;
        assertEquals(1, recipes.size());
        assertTrue(recipes.stream().anyMatch(h -> h.getFinalPrice() == 20));
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
        CreditCard creditCard = new CreditCard("1111222233334444", "11/23", "111");
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
        ATResponseObj<String> memberID = market.login(uuid, member);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        int numOfRecipes = market.getHistoryPurchase(uuid, existing_storeID).value.size();
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertTrue(response.errorOccurred());


        //post conditions
        ATResponseObj<String> managerID = market.login(uuid, member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID, item2);
        assertEquals(1, amountItem1);
        assertEquals(3, amountItem2);

        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);
        List<History> recipes = res.value;
        assertEquals(numOfRecipes, recipes.size());
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
        CreditCard creditCard = new CreditCard("XXX", "11/13", "111");
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
        assertTrue(market.addToCart(uuid, existing_storeID, item1));
        assertTrue(market.addToCart(uuid, existing_storeID, item2));
        ATResponseObj<String> memberID = market.login(uuid, member);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        int numOfRecipes = market.getHistoryPurchase(uuid, existing_storeID).value.size();
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertTrue(response.errorOccurred());

        //post conditions
        ATResponseObj<String> managerID = market.login(uuid, member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID, item2);
        assertEquals(1, amountItem1);
        assertEquals(3, amountItem2);


        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);
        List<History> recipes = res.value;
        assertEquals(numOfRecipes, recipes.size());

    }

    @Test
    @DisplayName("req: #2.2.5 - fail test [linear 2 buyers]")
    void purchaseCart_Fail3() {
        //use case:
        //1. Member adds to his cart item1 that the store has only 1 in stock, and logout.
        //2. Guest adds to his cart item1 and then purchases it. (expected success).
        //3. Member login, system will restore his cart. Then member try to purchase (expected fail - item not available).

        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        CreditCard creditCard = new CreditCard("1111222233334444", "11/25", "111");
        User registeredUser = generateUser();
        assertTrue(market.register(uuid, registeredUser.username, registeredUser.password, registeredUser.dateOfBirth));
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
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
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

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        assertEquals(0, amountItem1);


        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);
        List<History> recipes = res.value;
        assertEquals(1, recipes.size());


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

        ATResponseObj<String> response = market.purchaseCart(uuid, new CreditCard("xxx", "xx", "xxx"), new Address("x", "ss", -1));
        assertTrue(response.errorOccurred());

        //post conditions
        memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID, item2);
        assertEquals(1, amountItem1);
        assertEquals(3, amountItem2);


        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);
        List<History> recipes = res.value;
        assertEquals(numOfRecipes, recipes.size());
    }


    /**
     * Requirement: purchase cart  - #2.4.2
     */
    @Test
    @DisplayName("req: #2.2.5 with discount rule - failure test")
    void purchaseCartWithDiscountRule_failure() {
        //pre conditions
        ATResponseObj<String> ownerID = market.login(uuid, member);
        assertFalse(ownerID.errorOccurred());
        uuid = ownerID.value;
        DiscountRuleSL discountRule = new ConditionProductDiscountRuleSL(new ProductPredicateSL(2, 4, 10, true), 30);
        assertTrue(market.addNewDiscountRule(uuid, existing_storeID, discountRule));
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ItemDetail item2 = new ItemDetail("screenFULLHD", 2, 10, List.of("TV"), "screen");
        item2.itemID = SCREEN_FULL_HD_ID;
        assertTrue(market.addToCart(uuid, existing_storeID, item1));
        assertTrue(market.addToCart(uuid, existing_storeID, item2));

        CreditCard creditCard = new CreditCard("1111222233334444", "11/23", "111");
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertFalse(response.errorOccurred());

        //post conditions
        ATResponseObj<String> managerID = market.login(uuid, member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;
        assertEquals(1, market.getHistoryPurchase(uuid, existing_storeID).value.size());
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID, item2);
        assertEquals(0, amountItem1);
        assertEquals(1, amountItem2);

        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);//guest cannot call this func
        List<History> recipes = res.value;
        for (History h : recipes)
            System.out.println("TID: " + h.getTID() + " price: " + h.getFinalPrice());
        assertEquals(1, recipes.size());
        assertTrue(recipes.stream().anyMatch(h -> h.getFinalPrice() == 30));
    }

    /**
     * Requirement: purchase cart  - #2.4.2
     */
    @Test
    @DisplayName("req: #2.2.5 with buy rule - success test")
    void purchaseCartWithBuyRule_Success() {
        //pre conditions
        ATResponseObj<String> ownerID = market.login(uuid, member);//need to check that really owner
        assertFalse(ownerID.errorOccurred());
        uuid = ownerID.value;
        BuyRuleSL buyRule = new ProductBuyRuleSL(new ProductPredicateSL(1, 0, 10, true));
        assertTrue(market.addNewBuyRule(uuid, existing_storeID, buyRule));
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ItemDetail item2 = new ItemDetail("screenFULLHD", 1, 10, List.of("TV"), "screen");
        item2.itemID = SCREEN_FULL_HD_ID;
        assertTrue(market.addToCart(uuid, existing_storeID, item1));
        assertTrue(market.addToCart(uuid, existing_storeID, item2));

        CreditCard creditCard = new CreditCard("1111222233334444", "11/23", "111");
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertFalse(response.errorOccurred());
        String recipe = response.value;

        //post conditions
        ATResponseObj<String> managerID = market.login(uuid, member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;
        assertEquals(1, market.getHistoryPurchase(uuid, existing_storeID).value.size());
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID, item2);
        assertEquals(0, amountItem1);
        assertEquals(2, amountItem2);
        assertNotEquals("", recipe);
        assertNotNull(recipe);

        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);//guest cannot call this func
        List<History> recipes = res.value;
        assertEquals(1, recipes.size());
        assertTrue(recipes.stream().anyMatch(h -> h.getFinalPrice() == 20));
    }

    /*    */

    /**
     * Requirement: purchase cart  - #2.4.2
     */
    @Test
    @DisplayName("req: #2.2.5 with buy rule - failure test")
    void purchaseCartWithBuyRule_failure() {
        //pre conditions
        ATResponseObj<String> ownerID = market.login(uuid, member);//need to check that really owner
        assertFalse(ownerID.errorOccurred());
        uuid = ownerID.value;
        BuyRuleSL buyRule = new ProductBuyRuleSL(new ProductPredicateSL(1, 3, 10, false));
        assertTrue(market.addNewBuyRule(uuid, existing_storeID, buyRule));
        BuyRuleSL buyRule2 = new UserBuyRuleSL(new UserPredicateSL("guest"));
        assertTrue(market.addNewBuyRule(uuid, existing_storeID, buyRule2));
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ItemDetail item2 = new ItemDetail("screenFULLHD", 1, 10, List.of("TV"), "screen");
        item2.itemID = SCREEN_FULL_HD_ID;
        assertTrue(market.addToCart(uuid, existing_storeID, item1));
        assertTrue(market.addToCart(uuid, existing_storeID, item2));

        CreditCard creditCard = new CreditCard("1111222233334444", "11/23", "111");
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
//        boolean res = response.errorMsg.equals("error"); // need to check why it sometimes null
        assertEquals("error", response.errorMsg);
        //post conditions
        ATResponseObj<String> managerID = market.login(uuid, member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;
        assertEquals(0, market.getHistoryPurchase(uuid, existing_storeID).value.size());
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID, item2);
        assertEquals(1, amountItem1);
        assertEquals(3, amountItem2);

    }

    /**
     * Requirement: purchase cart  - #2.4.2
     */
    @Test
    @DisplayName("req: #2.2.5 with discount rule - success test")
    void purchaseCartWithDiscountRule_Success() {
        //pre conditions
        ATResponseObj<String> ownerID = market.login(uuid, member);
        assertFalse(ownerID.errorOccurred());
        uuid = ownerID.value;
        DiscountRuleSL discountRule = new SimpleStoreDiscountRuleSL(50);
        assertTrue(market.addNewDiscountRule(uuid, existing_storeID, discountRule));
        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;

        assertTrue(market.cartExists(uuid));
        assertTrue(market.guestOnline(uuid));
        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        ItemDetail item2 = new ItemDetail("screenFULLHD", 2, 10, List.of("TV"), "screen");
        item2.itemID = SCREEN_FULL_HD_ID;
        assertTrue(market.addToCart(uuid, existing_storeID, item1));
        assertTrue(market.addToCart(uuid, existing_storeID, item2));

        CreditCard creditCard = new CreditCard("1111222233334444", "11/23", "111");
        Address address = new Address("Tel-Aviv", "Nordau 3", 3);
        ATResponseObj<String> response = market.purchaseCart(uuid, creditCard, address);
        assertFalse(response.errorOccurred());

        //post conditions
        ATResponseObj<String> managerID = market.login(uuid, member);//manager of existing store
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;
        assertEquals(1, market.getHistoryPurchase(uuid, existing_storeID).value.size());
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        int amountItem2 = market.getAmountOfProductInStore(existing_storeID, item2);
        assertEquals(0, amountItem1);
        assertEquals(1, amountItem2);

        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);//guest cannot call this func
        List<History> recipes = res.value;
        assertEquals(1, recipes.size());
        assertTrue(recipes.stream().anyMatch(h -> h.getFinalPrice() == 15));
    }

    /**
     * Requirement: Purchase BID
     */

    @Test
    @DisplayName("Requirement: Purchase BID - success test")
    void purchaseBID_Success1() {
    // user create BID -> founder add new owner -> founder approve -> user try to buy(fail) -> newOwner approve -> buy success -> try buy again(fail)

    User userBID = generateUser();
    assertTrue(market.register(uuid, userBID.username, userBID.password, userBID.dateOfBirth));
    User newOwner = generateUser();
    assertTrue(market.register(uuid, newOwner.username, newOwner.password, newOwner.dateOfBirth));

    ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
    item1.itemID = IPHONE_5_ID;
    // userBID create BID
    ATResponseObj<String> userBIDID = market.login(uuid, userBID); //userBID is member
    assertFalse(userBIDID.errorOccurred());
    uuid =userBIDID.value;
    assertTrue(market.createBID(uuid,existing_storeID, IPHONE_5_ID ,1,100).value);

    ATResponseObj<String> founderID = market.logout(uuid);
    assertFalse(founderID.errorOccurred());
    uuid =founderID.value;
    founderID =market.login(uuid, member); //member is contributor
    //assign another owner
    assertTrue(market.assignNewOwner(founderID.value, existing_storeID, newOwner));
    assertTrue(market.isOwner(existing_storeID, newOwner));
    assertTrue(market.approveBID(founderID.value,userBID.username,existing_storeID,IPHONE_5_ID).value);

    ATResponseObj<String> userID = market.logout(founderID.value);
    assertFalse(userID.errorOccurred());
    uuid =userID.value;
    userID =market.login(uuid, userBID);

    ServiceCreditCard creditCard = new ServiceCreditCard("1111222233334444", "11/23", "111");
    assertTrue(market.BuyBID(userID.value,existing_storeID,IPHONE_5_ID,"haifa","ads",1,creditCard).errorOccurred());

    ATResponseObj<String> newOwnerID = market.logout(userID.value);
    assertFalse(newOwnerID.errorOccurred());
    uuid =newOwnerID.value;
    newOwnerID =market.login(uuid, newOwner);

    assertTrue(market.approveBID(newOwnerID.value,userBID.username,existing_storeID,IPHONE_5_ID).value);

    ATResponseObj<String> userID2 = market.logout(newOwnerID.value);
    assertFalse(userID2.errorOccurred());
    uuid =userID2.value;
    userID2 =market.login(uuid, userBID);

    assertTrue(market.BuyBID(userID2.value,existing_storeID,IPHONE_5_ID,"haifa","ads",1,creditCard).value);
    assertTrue(market.BuyBID(userID2.value,existing_storeID,IPHONE_5_ID,"haifa","ads",1,creditCard).errorOccurred());


    ATResponseObj<String> managerID = market.logout(userID2.value);
    assertFalse(managerID.errorOccurred());
    uuid =managerID.value;
    managerID = market.login(uuid, member);
    //post conditions
    assertFalse(managerID.errorOccurred());
    uuid = managerID.value;
    assertEquals(1, market.getHistoryPurchase(uuid, existing_storeID).value.size());
    int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
    assertEquals(0, amountItem1);

    ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);
    List<History> recipes = res.value;
    assertEquals(1, recipes.size());
    assertTrue(recipes.stream().anyMatch(h -> h.getFinalPrice() == 100));
}

    /**
     * Requirement: Purchase BID
     */

    @Test
    @DisplayName("Requirement: Purchase BID counter - success test")
    void purchaseBID_Success2() {
        // user create BID -> founder add new owner -> founder counter -> user confirm and try buy(fail) ->  newOwner approve -> user buy

        User userBID = generateUser();
        assertTrue(market.register(uuid, userBID.username, userBID.password, userBID.dateOfBirth));
        User newOwner = generateUser();
        assertTrue(market.register(uuid, newOwner.username, newOwner.password, newOwner.dateOfBirth));

        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        // userBID create BID
        ATResponseObj<String> userBIDID = market.login(uuid, userBID); //userBID is member
        assertFalse(userBIDID.errorOccurred());
        uuid =userBIDID.value;
        assertTrue(market.createBID(uuid,existing_storeID, IPHONE_5_ID ,1,100).value);

        ATResponseObj<String> founderID = market.logout(uuid);
        assertFalse(founderID.errorOccurred());
        uuid =founderID.value;
        founderID =market.login(uuid, member); //member is contributor
        //assign another owner
        assertTrue(market.assignNewOwner(founderID.value, existing_storeID, newOwner));
        assertTrue(market.isOwner(existing_storeID, newOwner));
        assertTrue(market.counterBID(founderID.value,userBID.username,existing_storeID,IPHONE_5_ID,500).value);

        ATResponseObj<String> userID = market.logout(founderID.value);
        assertFalse(userID.errorOccurred());
        uuid =userID.value;
        userID =market.login(uuid, userBID);

        ServiceCreditCard creditCard = new ServiceCreditCard("1111222233334444", "11/23", "111");
        assertTrue(market.BuyBID(userID.value,existing_storeID,IPHONE_5_ID,"haifa","ads",1,creditCard).errorOccurred());
        assertTrue(market.responseCounterBID(userID.value,existing_storeID,IPHONE_5_ID,true).value);
        assertTrue(market.BuyBID(userID.value,existing_storeID,IPHONE_5_ID,"haifa","ads",1,creditCard).errorOccurred());

        ATResponseObj<String> newOwnerID = market.logout(userID.value);
        assertFalse(newOwnerID.errorOccurred());
        uuid =newOwnerID.value;
        newOwnerID =market.login(uuid, newOwner);

        assertTrue(market.approveBID(newOwnerID.value,userBID.username,existing_storeID,IPHONE_5_ID).value);

        ATResponseObj<String> userID2 = market.logout(newOwnerID.value);
        assertFalse(userID2.errorOccurred());
        uuid =userID2.value;
        userID2 =market.login(uuid, userBID);

        assertTrue(market.BuyBID(userID2.value,existing_storeID,IPHONE_5_ID,"haifa","ads",1,creditCard).value);


        ATResponseObj<String> managerID = market.logout(userID2.value);
        assertFalse(managerID.errorOccurred());
        uuid =managerID.value;
        managerID = market.login(uuid, member);
        //post conditions
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;
        assertEquals(1, market.getHistoryPurchase(uuid, existing_storeID).value.size());
        int amountItem1 = market.getAmountOfProductInStore(existing_storeID, item1);
        assertEquals(0, amountItem1);

        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);
        List<History> recipes = res.value;
        assertEquals(1, recipes.size());
        assertTrue(recipes.stream().anyMatch(h -> h.getFinalPrice() == 500));
    }

    /**
     * Requirement: Purchase BID
     */

    @Test
    @DisplayName("Requirement: Purchase BID rejected fail - failure test")
    void purchaseBID_Fail() {
        // user create BID -> founder add new owner -> founder approve  ->  newOwner rejected -> user buy(fail) -> user remove

        User userBID = generateUser();
        assertTrue(market.register(uuid, userBID.username, userBID.password, userBID.dateOfBirth));
        User newOwner = generateUser();
        assertTrue(market.register(uuid, newOwner.username, newOwner.password, newOwner.dateOfBirth));

        ItemDetail item1 = new ItemDetail("iphone5", 1, 10, List.of("phone"), "phone");
        item1.itemID = IPHONE_5_ID;
        // userBID create BID
        ATResponseObj<String> userBIDID = market.login(uuid, userBID); //userBID is member
        assertFalse(userBIDID.errorOccurred());
        uuid =userBIDID.value;
        assertTrue(market.createBID(uuid,existing_storeID, IPHONE_5_ID ,1,100).value);

        ATResponseObj<String> founderID = market.logout(uuid);
        assertFalse(founderID.errorOccurred());
        uuid =founderID.value;
        founderID =market.login(uuid, member); //member is contributor
        //assign another owner
        assertTrue(market.assignNewOwner(founderID.value, existing_storeID, newOwner));
        assertTrue(market.isOwner(existing_storeID, newOwner));
        assertTrue(market.approveBID(founderID.value,userBID.username,existing_storeID,IPHONE_5_ID).value);
        assertEquals(1, market.getAllOffersBIDS(founderID.value, existing_storeID).value.size());

        ATResponseObj<String> userID = market.logout(founderID.value);
        assertFalse(userID.errorOccurred());
        uuid =userID.value;
        userID =market.login(uuid, userBID);

        ServiceCreditCard creditCard = new ServiceCreditCard("1111222233334444", "11/23", "111");
        assertTrue(market.BuyBID(userID.value,existing_storeID,IPHONE_5_ID,"haifa","ads",1,creditCard).errorOccurred());

        ATResponseObj<String> newOwnerID = market.logout(userID.value);
        assertFalse(newOwnerID.errorOccurred());
        uuid =newOwnerID.value;
        newOwnerID =market.login(uuid, newOwner);

        assertTrue(market.rejectBID(newOwnerID.value,userBID.username,existing_storeID,IPHONE_5_ID).value);

        ATResponseObj<String> userID2 = market.logout(newOwnerID.value);
        assertFalse(userID2.errorOccurred());
        uuid =userID2.value;
        userID2 =market.login(uuid, userBID);

        assertTrue(market.BuyBID(userID2.value,existing_storeID,IPHONE_5_ID,"haifa","ads",1,creditCard).errorOccurred());
        assertTrue(market.removeBID(userID2.value,existing_storeID,IPHONE_5_ID).value);

        ATResponseObj<String> managerID = market.logout(userID2.value);
        assertFalse(managerID.errorOccurred());
        uuid =managerID.value;
        managerID = market.login(uuid, member);
        //post conditions
        assertFalse(managerID.errorOccurred());
        uuid = managerID.value;
        assertEquals(0, market.getAllOffersBIDS(uuid, existing_storeID).value.size());



    }



}
