package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataBuyRule;
import com.example.demo.DataAccess.Entity.DataDiscountRule;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.BuyRules.*;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.DiscountRule.*;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class RuleServiceTest {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private StoreService storeService;


    @Test
    @Transactional
    void insertBuyRule() throws JsonProcessingException {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        BuyRule rule1 = new ShoppingBagBuyRule(new ShoppingBagPred(1,2,3));
        BuyRule rule2 = new UserBuyRule(new UserPred("dor@gmail.com"));
        BuyRule rule3 = new ProductBuyRule(new ProductPred(1,2,3,true));
        BuyRule or = new OrBuyRule(List.of(rule1,rule2));
        BuyRule rule4 = new CategoryBuyRule(new CategoryPred(1,2,3,4));
        BuyRule and = new AndBuyRule(List.of(or,rule3,rule4));
        BuyRule cond = new ConditioningBuyRule(or,and);

        DataBuyRule dataBuyRule = cond.getDataObject();
        assertNotEquals(-1,ruleService.insertBuyRule(dataBuyRule,dataStore.getStoreId()));

        //check get the json rule from db is create the correct BuyRule
        ObjectMapper mapper = new ObjectMapper();
        BuyRule convertedToObj = mapper.readValue(dataBuyRule.getRule(), BuyRule.class);
        assertTrue(convertedToObj instanceof ConditioningBuyRule);
        assertTrue(((ConditioningBuyRule) convertedToObj).getPredIf() instanceof  OrBuyRule);
        assertTrue(((ConditioningBuyRule) convertedToObj).getPredThen() instanceof  AndBuyRule);
        List<BuyRule> ifOrRules = ((OrBuyRule) ((ConditioningBuyRule) convertedToObj).getPredIf()).getRules();
        assertEquals(2, ifOrRules.size());
        assertTrue(ifOrRules.get(0) instanceof  ShoppingBagBuyRule);
        assertTrue(ifOrRules.get(1) instanceof  UserBuyRule);
        assertEquals(1, ((ShoppingBagBuyRule) ifOrRules.get(0)).getPred().getMinProductQuantity());
        assertEquals(2, ((ShoppingBagBuyRule) ifOrRules.get(0)).getPred().getMinProductTypes());
        assertEquals(3, ((ShoppingBagBuyRule) ifOrRules.get(0)).getPred().getTotalPrice());
        assertEquals("dor@gmail.com", ((UserBuyRule) ifOrRules.get(1)).getPred().getUsername());

    }

    @Test
    @Transactional
    void deleteBuyRule(){
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        BuyRule rule1 = new ShoppingBagBuyRule(new ShoppingBagPred(1,2,3));
        BuyRule rule2 = new UserBuyRule(new UserPred("dor@gmail.com"));
        BuyRule or = new OrBuyRule(List.of(rule1,rule2));
        DataBuyRule dataBuyRule = or.getDataObject();

        assertNotEquals(-1,ruleService.insertBuyRule(dataBuyRule,dataStore.getStoreId()));
        assertTrue(ruleService.deleteBuyRule(dataBuyRule.getBuyRuleId()));
        DataBuyRule nullBuyRule = ruleService.getBuyRuleByID(dataBuyRule.getBuyRuleId());
        assertNull(nullBuyRule);
    }


    @Test
    @Transactional
    void getBuyRuleById() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        BuyRule rule1 = new ShoppingBagBuyRule(new ShoppingBagPred(1,2,3));
        BuyRule rule2 = new UserBuyRule(new UserPred("dor@gmail.com"));
        BuyRule or = new OrBuyRule(List.of(rule1,rule2));
        DataBuyRule dataBuyRule = or.getDataObject();

        assertNotEquals(-1,ruleService.insertBuyRule(dataBuyRule,dataStore.getStoreId()));
        DataBuyRule dataBuyRule1 = ruleService.getBuyRuleByID(dataBuyRule.getBuyRuleId());
        //check
        assertEquals(dataBuyRule.getBuyRuleId(), dataBuyRule1.getBuyRuleId());
        assertEquals(dataBuyRule.getStore(), dataBuyRule1.getStore());
        assertEquals(dataBuyRule.getRule(), dataBuyRule1.getRule());
    }

    @Test
    @Transactional
    void getAllBuyRules() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        BuyRule rule1 = new ShoppingBagBuyRule(new ShoppingBagPred(1,2,3));
        BuyRule rule2 = new UserBuyRule(new UserPred("dor@gmail.com"));
        BuyRule rule3 = new OrBuyRule(List.of(rule1,rule2));

        List<DataBuyRule> dataBuyRules = List.of(
                rule1.getDataObject(),
                rule2.getDataObject(),
                rule3.getDataObject());
        dataBuyRules.forEach(dataBuyRule -> {
            assertNotEquals(-1,ruleService.insertBuyRule(dataBuyRule,dataStore.getStoreId()));
        });
        //action
        List<DataBuyRule> afterBuyRules = ruleService.getAllBuyRules();
        //check
        assertEquals(3, afterBuyRules.size());
        for (int i = 0; i < 3; i++) {
            DataBuyRule dataBuyRule = dataBuyRules.get(i);
            DataBuyRule afterBuyRule = afterBuyRules.get(i);
            //check
            assertEquals(dataBuyRule.getBuyRuleId(), afterBuyRule.getBuyRuleId());
            assertEquals(dataBuyRule.getStore(), afterBuyRule.getStore());
            assertEquals(dataBuyRule.getRule(), afterBuyRule.getRule());
        }
    }




    //****************************************************Discount*****************************************************


    @Test
    @Transactional
    void insertDiscountRule() throws JsonProcessingException {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        DiscountRule rule1 = new ConditionStoreDiscountRule(new ShoppingBagPred(1,2,3),50);
        DiscountRule rule2 = new ConditionCategoryDiscountRule(new CategoryPred(1,17,2,20),40);
        DiscountRule or = new OrDiscountRule(List.of(rule1,rule2),1,60);
        DiscountRule rule3 = new ConditionProductDiscountRule(new ProductPred(1,2,3),30);
        DiscountRule rule4 = new AddDiscountRule(List.of(rule1,rule2));
        DiscountRule and = new AndDiscountRule(List.of(or,rule3,rule4),2,90);
        DiscountRule xor = new XorDiscountRule(List.of(and,rule1),"Big Discount");

        DataDiscountRule dataDiscountRule = xor.getDataObject();
        assertNotEquals(-1,ruleService.insertDiscountRule(dataDiscountRule,dataStore.getStoreId()));

        //check get the json rule from db is create the correct BuyRule
        ObjectMapper mapper = new ObjectMapper();
        DiscountRule convertedToObj = mapper.readValue(dataDiscountRule.getRule(), DiscountRule.class);

        //check convert to object good
        assertTrue(convertedToObj instanceof XorDiscountRule);
        assertEquals(0.0, convertedToObj.getPercentDiscount());
        assertEquals("Big Discount", ((XorDiscountRule) convertedToObj).getDecision());
        List<DiscountRule> xorRules = ((XorDiscountRule) convertedToObj).getRules();
        assertTrue(xorRules.get(0) instanceof  AndDiscountRule);
        assertTrue(xorRules.get(1) instanceof  ConditionStoreDiscountRule);
        assertEquals(1, ((ConditionStoreDiscountRule) xorRules.get(1)).getPred().getMinProductQuantity());
        assertEquals(2, ((ConditionStoreDiscountRule) xorRules.get(1)).getPred().getMinProductTypes());
        assertEquals(3, ((ConditionStoreDiscountRule) xorRules.get(1)).getPred().getTotalPrice());
        assertEquals(2, ((AndDiscountRule) xorRules.get(0)).getCategory());
        assertEquals(90, ((AndDiscountRule) xorRules.get(0)).getDiscount());
        List<DiscountRule> andRules = ((AndDiscountRule) xorRules.get(0)).getRules();
        assertTrue(andRules.get(0) instanceof  OrDiscountRule);
        assertTrue(andRules.get(1) instanceof  ConditionProductDiscountRule);
        assertTrue(andRules.get(2) instanceof  AddDiscountRule);
        assertEquals(1, ((ConditionProductDiscountRule) andRules.get(1)).getPred().getProductID());
        assertEquals(2, ((ConditionProductDiscountRule) andRules.get(1)).getPred().getMinQuantity());
        assertEquals(3, ((ConditionProductDiscountRule) andRules.get(1)).getPred().getMaxQuantity());
        assertEquals(30,andRules.get(1).getPercentDiscount());
        assertEquals(1, ((OrDiscountRule) andRules.get(0)).getCategory());
        assertEquals(60, ((OrDiscountRule) andRules.get(0)).getDiscount());
        List<DiscountRule> orRules = ((OrDiscountRule) andRules.get(0)).getRules();
        assertTrue(orRules.get(1) instanceof  ConditionCategoryDiscountRule);
        assertEquals(40, orRules.get(1).getPercentDiscount());
        assertEquals(1, ((ConditionCategoryDiscountRule) orRules.get(1)).getPred().getCategory());
        assertEquals(17, ((ConditionCategoryDiscountRule) orRules.get(1)).getPred().getMinAge());
        assertEquals(2, ((ConditionCategoryDiscountRule) orRules.get(1)).getPred().getMinHour());
        assertEquals(20, ((ConditionCategoryDiscountRule) orRules.get(1)).getPred().getMaxHour());

    }



    @Test
    @Transactional
    void insertXORDiscountRule() throws JsonProcessingException {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        DiscountRule rule1 = new ConditionStoreDiscountRule(new ShoppingBagPred(1,2,3),50);
        DiscountRule rule2 = new ConditionCategoryDiscountRule(new CategoryPred(1,17,2,20),40);
        DiscountRule or = new OrDiscountRule(List.of(rule1,rule2),1,60);
        DiscountRule rule3 = new ConditionProductDiscountRule(new ProductPred(1,2,3),30);
        DiscountRule rule4 = new AddDiscountRule(List.of(rule1,rule2));
        DiscountRule and = new AndDiscountRule(List.of(rule3,rule4),2,90);

        DiscountPolicy discountPolicy = new DiscountPolicy();
        discountPolicy.addNewDiscountRule(and,1);
        discountPolicy.addNewDiscountRule(or,1);
        assertNotNull(ruleService.getDiscountRuleByID(1));
        assertNotNull(ruleService.getDiscountRuleByID(2));

        discountPolicy.combineXORDiscountRules(List.of(1,2),"first",1);
        assertNull(ruleService.getDiscountRuleByID(1));
        assertNull(ruleService.getDiscountRuleByID(2));
        DataDiscountRule xor = ruleService.getDiscountRuleByID(3);

        //check get the json rule from db is create the correct BuyRule
        ObjectMapper mapper = new ObjectMapper();
        DiscountRule convertedToObj = mapper.readValue(xor.getRule(), DiscountRule.class);

        //check convert to object good
        assertTrue(convertedToObj instanceof XorDiscountRule);
        assertEquals(0.0, convertedToObj.getPercentDiscount());
        assertEquals("first", ((XorDiscountRule) convertedToObj).getDecision());
        List<DiscountRule> xorRules = ((XorDiscountRule) convertedToObj).getRules();
        assertTrue(xorRules.get(0) instanceof  AndDiscountRule);
        assertTrue(xorRules.get(1) instanceof  OrDiscountRule);
    }

    @Test
    @Transactional
    void insertANDORDiscountRule() throws JsonProcessingException {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        DiscountRule rule1 = new ConditionStoreDiscountRule(new ShoppingBagPred(1,2,3),50);
        DiscountRule rule2 = new ConditionCategoryDiscountRule(new CategoryPred(1,17,2,20),40);
        DiscountRule or = new OrDiscountRule(List.of(rule1,rule2),1,60);
        DiscountRule rule3 = new ConditionProductDiscountRule(new ProductPred(1,2,3),30);
        DiscountRule rule4 = new AddDiscountRule(List.of(rule1,rule2));
        DiscountRule and = new AndDiscountRule(List.of(rule3,rule4),2,90);

        DiscountPolicy discountPolicy = new DiscountPolicy();
        discountPolicy.addNewDiscountRule(and,1);
        discountPolicy.addNewDiscountRule(or,1);
        assertNotNull(ruleService.getDiscountRuleByID(1));
        assertNotNull(ruleService.getDiscountRuleByID(2));

        discountPolicy.combineANDORDiscountRules("or",List.of(1,2),3,50,1);
        assertNull(ruleService.getDiscountRuleByID(1));
        assertNull(ruleService.getDiscountRuleByID(2));
        DataDiscountRule orCombined = ruleService.getDiscountRuleByID(3);

        //check get the json rule from db is create the correct BuyRule
        ObjectMapper mapper = new ObjectMapper();
        DiscountRule convertedToObj = mapper.readValue(orCombined.getRule(), DiscountRule.class);

        //check convert to object good
        assertTrue(convertedToObj instanceof OrDiscountRule);
        assertEquals(50, convertedToObj.getPercentDiscount());
        assertEquals(3, ((OrDiscountRule) convertedToObj).getCategory());
        List<DiscountRule> orCombinedRules = ((OrDiscountRule) convertedToObj).getRules();
        assertTrue(orCombinedRules.get(0) instanceof  AndDiscountRule);
        assertTrue(orCombinedRules.get(1) instanceof  OrDiscountRule);
    }


    @Test
    @Transactional
    void deleteDiscountRule(){
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        DiscountRule rule1 = new ConditionStoreDiscountRule(new ShoppingBagPred(1,2,3),50);
        DiscountRule rule2 = new ConditionCategoryDiscountRule(new CategoryPred(1,17,2,20),40);
        DiscountRule or = new OrDiscountRule(List.of(rule1,rule2),1,60);
        DataDiscountRule dataDiscountRule = or.getDataObject();

        assertNotEquals(-1,ruleService.insertDiscountRule(dataDiscountRule,dataStore.getStoreId()));
        assertTrue(ruleService.deleteDiscountRule(dataDiscountRule.getDiscountRuleId()));
        DataDiscountRule nullDiscountRule = ruleService.getDiscountRuleByID(dataDiscountRule.getDiscountRuleId());
        assertNull(nullDiscountRule);
    }


    @Test
    @Transactional
    void getDiscountRuleById() throws JsonProcessingException {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        DiscountRule rule1 = new SimpleProductDiscountRule(50,1);
        DiscountRule rule2 = new SimpleCategoryDiscountRule(40,3);
        DiscountRule rule3 = new SimpleStoreDiscountRule(10);
        DiscountRule or = new OrDiscountRule(List.of(rule1,rule2,rule3),1,60);
        DataDiscountRule dataDiscountRule = or.getDataObject();

        assertNotEquals(-1,ruleService.insertDiscountRule(dataDiscountRule,dataStore.getStoreId()));
        DataDiscountRule DiscountRule1 = ruleService.getDiscountRuleByID(dataDiscountRule.getDiscountRuleId());

        //check
        assertEquals(dataDiscountRule.getDiscountRuleId(), DiscountRule1.getDiscountRuleId());
        assertEquals(dataDiscountRule.getStore(), DiscountRule1.getStore());
        assertEquals(dataDiscountRule.getRule(), DiscountRule1.getRule());

        //check get the json rule from db is create the correct BuyRule
        ObjectMapper mapper = new ObjectMapper();
        DiscountRule convertedToObj = mapper.readValue(dataDiscountRule.getRule(), DiscountRule.class);

        assertTrue(convertedToObj instanceof  OrDiscountRule);
        assertEquals(1, ((OrDiscountRule) convertedToObj).getCategory());
        assertEquals(60, convertedToObj.getPercentDiscount());
        List<DiscountRule> orRules = ((OrDiscountRule) convertedToObj).getRules();
        assertTrue(orRules.get(0) instanceof  SimpleProductDiscountRule);
        assertTrue(orRules.get(1) instanceof  SimpleCategoryDiscountRule);
        assertTrue(orRules.get(2) instanceof  SimpleStoreDiscountRule);
        assertEquals(50, (orRules.get(0)).getPercentDiscount());
        assertEquals(1, ((SimpleProductDiscountRule) orRules.get(0)).getProductId());
        assertEquals(40, (orRules.get(1)).getPercentDiscount());
        assertEquals(3, ((SimpleCategoryDiscountRule) orRules.get(1)).getCategoryId());
        assertEquals(10, (orRules.get(2)).getPercentDiscount());
    }

    @Test
    @Transactional
    void getAllDiscountRules() {
        Store store = new Store("myStore", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        DataStore dataStore = store.getDataObject();
        storeService.insertStore(dataStore);

        DiscountRule rule1 = new ConditionStoreDiscountRule(new ShoppingBagPred(1,2,3),50);
        DiscountRule rule2 = new ConditionCategoryDiscountRule(new CategoryPred(1,17,2,20),40);
        DiscountRule rule3 = new OrDiscountRule(List.of(rule1,rule2),1,60);

        List<DataDiscountRule> dataDiscountRules = List.of(
                rule1.getDataObject(),
                rule2.getDataObject(),
                rule3.getDataObject());
        dataDiscountRules.forEach(dataDiscountRule -> {
            assertNotEquals(-1,ruleService.insertDiscountRule(dataDiscountRule,dataStore.getStoreId()));
        });
        //action
        List<DataDiscountRule> afterDiscountRules = ruleService.getAllDiscountRules();
        //check
        assertEquals(3, afterDiscountRules.size());
        for (int i = 0; i < 3; i++) {
            DataDiscountRule dataDiscountRule = dataDiscountRules.get(i);
            DataDiscountRule afterDiscountRule = afterDiscountRules.get(i);
            //check
            assertEquals(dataDiscountRule.getDiscountRuleId(), afterDiscountRule.getDiscountRuleId());
            assertEquals(dataDiscountRule.getStore(), afterDiscountRule.getStore());
            assertEquals(dataDiscountRule.getRule(), afterDiscountRule.getRule());
        }
    }



}