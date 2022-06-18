package com.example.Acceptance.Tests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.Acceptance.Obj.*;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.SimpleCategoryDiscountRule;
import com.example.demo.Service.ServiceObj.BuyRules.*;
import com.example.demo.Service.ServiceObj.DiscountRules.*;
import com.example.demo.Service.ServiceObj.Predicate.CategoryPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.ProductPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.ShoppingBagPredicateSL;
import com.example.demo.Service.ServiceObj.Predicate.UserPredicateSL;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
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
        market.resetMemory();
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
        assertEquals(150, response.value.price);
        assertEquals(3, response.value.quantity);
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
        assertEquals(60.0, response.value.price);
        assertEquals(1, response.value.quantity);
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
        assertEquals(60.0, response.value.price);
        assertEquals(1, response.value.quantity);
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
     *
     * Requirement: policies of buying and discounts  - #2.4.2
     */
    @Test
    @DisplayName("req: #2.4.2 - success test")
    void add_remove_buy_policy_Success() {
        BuyRuleSL userRule = new UserBuyRuleSL(new UserPredicateSL("osnat@gmail.com"));
        BuyRuleSL productRule = new ProductBuyRuleSL(new ProductPredicateSL(1,2,5));
        List<BuyRuleSL> rules = new ArrayList<>();
        rules.add(userRule); rules.add(productRule);
        BuyRuleSL and = new AndBuyRuleSL(rules);

        assertTrue(market.isOwner(existing_storeID,member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertEquals(0, market.getBuyPolicy(uuid, existing_storeID).value.size());
        assertTrue(market.addNewBuyRule(uuid, existing_storeID, and));
        assertEquals(1, market.getBuyPolicy(uuid, existing_storeID).value.size());

        BuyRuleSL categoryBR = new CategoryBuyRuleSL(new CategoryPredicateSL(1,18,0,24));
        assertTrue(market.addNewBuyRule(uuid, existing_storeID, categoryBR));
        assertEquals(2, market.getBuyPolicy(uuid, existing_storeID).value.size());

        assertFalse(market.removeBuyRule(uuid, existing_storeID, 3)); // 3 id is not in buy policy
        assertEquals(2, market.getBuyPolicy(uuid, existing_storeID).value.size());
        assertTrue(market.removeBuyRule(uuid, existing_storeID, 2));
        assertEquals(1, market.getBuyPolicy(uuid, existing_storeID).value.size());
        assertTrue(market.removeBuyRule(uuid, existing_storeID, 1));
        assertEquals(0, market.getBuyPolicy(uuid, existing_storeID).value.size());

    }



    @Test
    @DisplayName("req: #2.4.2 - fail test [...]")
    void add_remove_buy_policy_Fail() {
        BuyRuleSL userRule = new UserBuyRuleSL(new UserPredicateSL("osnat@gmail.com"));
        BuyRuleSL productRule = new ProductBuyRuleSL(new ProductPredicateSL(1,2,5));
        List<BuyRuleSL> rules = new ArrayList<>();
        rules.add(userRule); rules.add(productRule);
        BuyRuleSL or = new OrBuyRuleSL(rules);

        User newMem = generateUser();
        assertTrue(market.register(uuid, newMem.username, newMem.password,newMem.dateOfBirth));
        assertFalse(market.isOwner(existing_storeID,newMem));
        assertTrue(market.isMember(newMem));

        ATResponseObj<String> newMemID = market.login(uuid, newMem); //newMem is not owner
        assertFalse(newMemID.errorOccurred());
        uuid = newMemID.value;

        assertFalse(market.addNewBuyRule(uuid, existing_storeID, or));  // not owner
        assertEquals(0,market.getBuyPolicy(uuid, existing_storeID).value.size());

        assertFalse(market.removeBuyRule(uuid, existing_storeID, 1)); // not owner
        assertEquals(0,market.getBuyPolicy(uuid, existing_storeID).value.size());

    }

    @Test
    @DisplayName("req: #2.4.2 - success test")
    void add_remove_discount_policy_Success() {
        DiscountRuleSL storeDiscountRule = new ConditionStoreDiscountRuleSL(new ShoppingBagPredicateSL(1,2,100),50);
        DiscountRuleSL productDiscountRule = new ConditionProductDiscountRuleSL(new ProductPredicateSL(1,2,5),10);
        List<DiscountRuleSL> rules = new ArrayList<>();
        rules.add(storeDiscountRule); rules.add(productDiscountRule);
        DiscountRuleSL and = new AndDiscountRuleSL(rules,1,50);

        assertTrue(market.isOwner(existing_storeID,member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertEquals(0, market.getDiscountPolicy(uuid, existing_storeID).value.size());
        assertTrue(market.addNewDiscountRule(uuid, existing_storeID, and));
        assertEquals(1, market.getDiscountPolicy(uuid, existing_storeID).value.size());

        DiscountRuleSL categoryDR = new SimpleCategoryDiscountRuleSL(1,30);
        assertTrue(market.addNewDiscountRule(uuid, existing_storeID, categoryDR));
        assertEquals(2, market.getDiscountPolicy(uuid, existing_storeID).value.size());

        assertFalse(market.removeDiscountRule(uuid, existing_storeID, 3)); // 3 id is not in buy policy
        assertEquals(2, market.getDiscountPolicy(uuid, existing_storeID).value.size());
        assertTrue(market.removeDiscountRule(uuid, existing_storeID, 2));
        assertEquals(1, market.getDiscountPolicy(uuid, existing_storeID).value.size());
        assertTrue(market.removeDiscountRule(uuid, existing_storeID, 1));
        assertEquals(0, market.getDiscountPolicy(uuid, existing_storeID).value.size());

    }



    @Test
    @DisplayName("req: #2.4.2 - fail test [...]")
    void add_remove_discount_policy_Success_Fail() {
        DiscountRuleSL storeDiscountRule = new ConditionStoreDiscountRuleSL(new ShoppingBagPredicateSL(1,2,100),50);
        DiscountRuleSL productDiscountRule = new ConditionProductDiscountRuleSL(new ProductPredicateSL(1,2,5),10);
        List<DiscountRuleSL> rules = new ArrayList<>();
        rules.add(storeDiscountRule); rules.add(productDiscountRule);
        DiscountRuleSL or = new OrDiscountRuleSL(rules,1,50);

        User newMem = generateUser();
        assertTrue(market.register(uuid, newMem.username, newMem.password,newMem.dateOfBirth));
        assertFalse(market.isOwner(existing_storeID,newMem));
        assertTrue(market.isMember(newMem));

        ATResponseObj<String> newMemID = market.login(uuid, newMem); //newMem is not owner
        assertFalse(newMemID.errorOccurred());
        uuid = newMemID.value;

        assertFalse(market.addNewDiscountRule(uuid, existing_storeID, or));  // not owner
        assertEquals(0,market.getDiscountPolicy(uuid, existing_storeID).value.size());

        assertFalse(market.removeDiscountRule(uuid, existing_storeID, 1)); // not owner
        assertEquals(0,market.getDiscountPolicy(uuid, existing_storeID).value.size());

    }

    @Test
    @DisplayName("req: #2.4.2 - fail test [...]")
    void add_discount_policy_Success_Fail2() {
        DiscountRuleSL storeDiscountRule = new ConditionStoreDiscountRuleSL(new ShoppingBagPredicateSL(1,2,100),50);
        DiscountRuleSL productDiscountRule = new ConditionProductDiscountRuleSL(new ProductPredicateSL(1,2,5),10);
        List<DiscountRuleSL> rules = new ArrayList<>();
        rules.add(storeDiscountRule); rules.add(productDiscountRule);
        DiscountRuleSL and = new AndDiscountRuleSL(rules,1,200);

        assertTrue(market.isOwner(existing_storeID,member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertEquals(0, market.getDiscountPolicy(uuid, existing_storeID).value.size());
        assertFalse(market.addNewDiscountRule(uuid, existing_storeID, and)); // discount not between 0-100
        assertEquals(0, market.getDiscountPolicy(uuid, existing_storeID).value.size());

        DiscountRuleSL categoryDR = new SimpleCategoryDiscountRuleSL(-5,30);
        assertFalse(market.addNewDiscountRule(uuid, existing_storeID, categoryDR)); // discount not between 0-100
        assertEquals(0, market.getDiscountPolicy(uuid, existing_storeID).value.size());

    }


    /**
     * Requirement: assign store owner - #2.4.4
     */
    @Test
    @DisplayName("req: #2.4.4 - success test")
    void assignStoreOwner_Success() {
        User newOwner = generateUser();
        assertTrue(market.register(uuid, newOwner.username, newOwner.password,newOwner.dateOfBirth));
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
//        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
//        assertFalse(memberID.errorOccurred());
//        uuid = memberID.value;
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
        assertTrue(market.register(uuid, newOwner.username, newOwner.password,newOwner.dateOfBirth));
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
     * Requirement: remove store owner - #2.4.5
     */

    @Test
    @DisplayName("req: #2.4.5 - success test")
    void removeStoreOwner_Success() {
        User newOwner = generateUser();
        assertTrue(market.register(uuid, newOwner.username, newOwner.password,newOwner.dateOfBirth));
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

        assertTrue(market.removeStoreOwner(uuid,existing_storeID,newOwner.username));
        assertFalse(market.isOwner(existing_storeID, newOwner));
        assertTrue(market.isOwner(existing_storeID, member));
    }


    @Test
    @DisplayName("req: #2.4.5 - fail test")
    void removeStoreOwner_Fail1() {
        User newOwner = generateUser();
        assertTrue(market.register(uuid, newOwner.username, newOwner.password,newOwner.dateOfBirth));
        assertTrue(market.isMember(newOwner));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newOwner));

        assertFalse(market.removeStoreOwner(uuid,existing_storeID,newOwner.username));
        assertFalse(market.isOwner(existing_storeID, newOwner));
        assertTrue(market.isOwner(existing_storeID, member));
    }


    @Test
    @DisplayName("req: #2.4.5 - fail test")
    void removeStoreOwner_Fail2() {
        User newOwner = generateUser();
        assertTrue(market.register(uuid, newOwner.username, newOwner.password,newOwner.dateOfBirth));
        User newOwner2 = generateUser();
        assertTrue(market.register(uuid, newOwner2.username, newOwner2.password,newOwner2.dateOfBirth));
        assertTrue(market.isMember(newOwner2));
        assertTrue(market.isMember(newOwner));
        assertTrue(market.isMember(member));


        //founder give permission for owner1
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newOwner));

        assertTrue(market.assignNewOwner(uuid, existing_storeID, newOwner));

        assertTrue(market.isOwner(existing_storeID, newOwner));
        assertTrue(market.isOwner(existing_storeID, member));


        ATResponseObj<String> ownerID = market.logout(uuid);
        assertFalse(ownerID.errorOccurred());
        uuid = ownerID.value;
        ownerID = market.login(uuid, newOwner);

        //owner1 give permission for owner2

        assertTrue(market.assignNewOwner(ownerID.value, existing_storeID, newOwner2));

        assertTrue(market.isOwner(existing_storeID, newOwner2));
        assertTrue(market.isOwner(existing_storeID, newOwner));

        ATResponseObj<String> founderID = market.logout(ownerID.value);
        assertFalse(founderID.errorOccurred());
        uuid = founderID.value;
        founderID = market.login(uuid, member);

        // founder remove owner1 permission and need rec to remove owner2
        assertFalse(market.removeStoreOwner(founderID.value,existing_storeID,newOwner2.username));
        assertTrue(market.isOwner(existing_storeID, newOwner2));
        assertTrue(market.isOwner(existing_storeID, newOwner));
        assertTrue(market.isOwner(existing_storeID, member));
    }


    /**
     * Requirement: assign store manager - #2.4.6
     */
    @Test
    @DisplayName("req: #2.4.6 - success test")
    void assignStoreManager_Success() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password, newManager.dateOfBirth));
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
        assertTrue(market.register(uuid, newManager.username, newManager.password, newManager.dateOfBirth));
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
        assertTrue(market.register(uuid, newManager.username, newManager.password, newManager.dateOfBirth));
        assertTrue(market.isMember(newManager));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID, member));
        assertFalse(market.isOwner(existing_storeID, newManager));
        assertFalse(market.isManager(existing_storeID, newManager));

        assertFalse(market.assignNewManager(uuid, existing_storeID + 60, newManager));

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
        assertTrue(market.register(uuid, newManager.username, newManager.password, newManager.dateOfBirth));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.assignNewManager(uuid, existing_storeID, newManager));
        assertTrue(market.isManager(existing_storeID, newManager));

        ATResponseObj<List<History>> historyPurchase = market.getHistoryPurchase(uuid, existing_storeID);
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
        assertTrue(market.register(uuid, newManager.username, newManager.password, newManager.dateOfBirth));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.assignNewManager(uuid, existing_storeID, newManager));
        assertTrue(market.isManager(existing_storeID, newManager));

        ATResponseObj<List<History>> historyPurchase = market.getHistoryPurchase(uuid, existing_storeID);
        assertFalse(historyPurchase.errorOccurred());

        assertFalse(market.updatePermission(uuid, Permission.GET_ORDER_HISTORY, false, newManager, -1));
    }

    @Test
    @DisplayName("req: #2.4.7 - fail test [user is not owner of store]")
    void managerPermissionChange_Fail2() {
        User newManager = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password, newManager.dateOfBirth));
        ATResponseObj<String> res = market.login(uuid,newManager);
        assertFalse(res.errorOccurred());
        uuid = res.value;
        assertFalse(market.updatePermission(uuid, Permission.GET_ORDER_HISTORY, false, newManager, existing_storeID));
    }

    @Test
    @DisplayName("req: #2.4.7 - fail test [invalid permissions]")
    void managerPermissionChange_Fail3() {
        User newManager = generateUser();
        User user = generateUser();
        assertTrue(market.register(uuid, newManager.username, newManager.password, newManager.dateOfBirth));
        assertTrue(market.register(uuid, user.username, user.password, user.dateOfBirth));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.assignNewManager(uuid, existing_storeID, newManager));
        assertTrue(market.isManager(existing_storeID, newManager));

        ATResponseObj<List<History>> historyPurchase = market.getHistoryPurchase(uuid, existing_storeID);
        assertFalse(historyPurchase.errorOccurred());

        assertFalse(market.updatePermission(uuid, "killTheManager", false, newManager, existing_storeID));

        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;
        memberID = market.login(uuid, newManager);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertFalse(market.assignNewManager(uuid,existing_storeID,user));

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
    void closeAndReopenStore_Success() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.closeStore(uuid, existing_storeID));
        assertTrue(market.storeIsClosed(existing_storeID));
        ItemDetail item = new ItemDetail("iphoneZX", 1, 10, List.of("phone"), "phone");
        item.itemID = 1122;
        assertFalse(market.addItemToStore(uuid, existing_storeID,item));
        assertTrue(market.reopenStore(uuid, existing_storeID));
        assertFalse(market.storeIsClosed(existing_storeID));
        assertTrue(market.addNewBuyRule(uuid,existing_storeID,new UserBuyRuleSL(new UserPredicateSL("niv@gmail.com"))));
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
        assertTrue(market.register(uuid, newManager.username, newManager.password, newManager.dateOfBirth));
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
    @DisplayName("req: #2.4.9 - fail test [user doesnt have permission]")
    void closeAndReopenStore_Fail3() {
        User user = generateUser();
        assertTrue(market.register(uuid, user.username, user.password, user.dateOfBirth));
        assertTrue(market.isMember(member));
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;

        assertTrue(market.closeStore(uuid,existing_storeID));

        ATResponseObj<String> guestID = market.logout(uuid);
        assertFalse(guestID.errorOccurred());
        uuid = guestID.value;
        memberID = market.login(uuid, user);
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.storeIsClosed(existing_storeID));
        assertFalse(market.reopenStore(uuid, existing_storeID));
        assertTrue(market.storeIsClosed(existing_storeID));
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
        assertTrue(market.register(uuid, newManager.username, newManager.password, newManager.dateOfBirth));
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
        ATResponseObj<String> res = market.getUserRoleInfo(uuid,existing_storeID);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.11 - fail test [user doesnt exist]")
    void getRoleInfo_Fail3() {
        ATResponseObj<String> res = market.getUserRoleInfo(uuid,existing_storeID);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.11 - fail test [invalid input]")
    void getRoleInfo_Fail4() {
        ATResponseObj<String> res = market.getUserRoleInfo(uuid,-1);
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
        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);
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
        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID+50);
        assertTrue(res.errorOccurred());
    }
    @Test
    @DisplayName("req: #2.4.13 - fail test [no permission]")
    void purchaseHistory_Fail2() {
        User user = generateUser();
        assertTrue(market.register(uuid, user.username, user.password,user.dateOfBirth));
        ATResponseObj<String> memberID = market.login(uuid, user); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, existing_storeID);
        assertTrue(res.errorOccurred());
    }

    @Test
    @DisplayName("req: #2.4.13 - fail test [...]")
    void purchaseHistory_Fail3() {
        ATResponseObj<String> memberID = market.login(uuid, member); //member is contributor
        assertFalse(memberID.errorOccurred());
        uuid = memberID.value;
        assertTrue(market.isOwner(existing_storeID,member));
        ATResponseObj<List<History>> res = market.getHistoryPurchase(uuid, -1);
        assertTrue(res.errorOccurred());
    }


}
