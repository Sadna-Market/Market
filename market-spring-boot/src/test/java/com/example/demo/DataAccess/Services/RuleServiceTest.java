package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataBuyRule;
import com.example.demo.DataAccess.Entity.DataDiscountRule;
import com.example.demo.DataAccess.Entity.DataStore;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.BuyRules.*;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.DiscountRule.OrDiscountRule;
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

    DataStore dataStore;




    @Test
    @Transactional
    void insertBuyRule(){
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
    //@Transactional
    void insertDiscountRule() {
        String rule = "this is a rule";
        Store store1 = new Store("myStore1", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        var dataStore = store1.getDataObject();
        assertTrue(storeService.insertStore(dataStore));

        /*
        * DataDiscountRule ddr = ruleBL.getDataObject();
        * */
        DataDiscountRule rule1 = new DataDiscountRule();
        rule1.setRule(rule);
        rule1.setStore(dataStore);
        assertTrue(ruleService.insertDiscountRule(rule1, dataStore.getStoreId()));
    }

    /*@Test
    //@Transactional
    void insertBuyPolicy() throws JsonProcessingException {
        Store store1 = new Store("myStore1", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        var dataStore = store1.getDataObject();
        assertTrue(storeService.insertStore(dataStore));

        *//*
           Rule r = new Rule();
         * DataDiscountRule ddr = ruleBL.getDataObject();
         * ruleService.insertDiscountRule(ddr);
         * r.setId(ddr.getId); generated id
         * *//*

        BuyRule u = new UserBuyRule(new UserPred("dor@gmail.com"));
        BuyRule u2 = new UserBuyRule(new UserPred("daniel@gmail.com"));
        BuyRule or = new OrBuyRule(List.of(u,u2));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(u);

        DataBuyRule dataBuyRule = new DataBuyRule();
        dataBuyRule.setRule(json);
        dataBuyRule.setStore(dataStore);
        assertTrue(ruleService.insertBuyRule(dataBuyRule,1));
        int x =3;
        //DataBuyRule dataBuyRule1 = ruleService.getBuyRuleByID(dataBuyRule.getBuyRuleId());
        //check
*//*        assertEquals(dataBuyRule.getBuyRuleId(), dataBuyRule1.getBuyRuleId());
        assertEquals(dataBuyRule.getStore(), dataBuyRule1.getStore());
        assertEquals(dataBuyRule.getRule(), dataBuyRule1.getRule());*//*

    }*/
/*
    @Test
    //@Transactional
    void getBuyRuleID() throws JsonProcessingException {
        Store store1 = new Store("myStore1", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        var dataStore = store1.getDataObject();
        assertTrue(storeService.insertStore(dataStore));

        BuyRule rule = new ShoppingBagBuyRule(new ShoppingBagPred(1,2,3));
        BuyRule rule2 = new UserBuyRule(new UserPred("dor@gmail.com"));
        BuyRule rule3 = new ProductBuyRule(new ProductPred(1,2,3,true));

        BuyRule or = new OrBuyRule(List.of(rule,rule2));
        BuyRule rule4 = new CategoryBuyRule(new CategoryPred(1,2,3,4));
        BuyRule and = new AndBuyRule(List.of(or,rule3,rule4));

        BuyRule cond = new ConditioningBuyRule(or,and);

        DataBuyRule dataBuyRule = cond.getDataObject();
        assertTrue(ruleService.insertBuyRule(dataBuyRule,dataStore.getStoreId()));
        cond.setID(dataBuyRule.getBuyRuleId());
        *//*
           Rule r = new Rule();
         * DataDiscountRule ddr = ruleBL.getDataObject();
         * ruleService.insertDiscountRule(ddr);
         * r.setId(ddr.getId); generated id
         * *//*


        //action
        DataBuyRule dataBuyRule1 = ruleService.getBuyRuleByID(dataBuyRule.getBuyRuleId());
        //check
        assertEquals(dataBuyRule.getBuyRuleId(), dataBuyRule1.getBuyRuleId());
        //assertEquals(dataBuyRule.getStore(), dataBuyRule1.getStore());
        assertEquals(dataBuyRule.getRule(), dataBuyRule1.getRule());

*//*
        mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
*//*

        ObjectMapper mapper = new ObjectMapper();
        //OrBuyRule link = mapper.readValue(dataBuyRule1.getRule(), OrBuyRule.class);
        BuyRule link = mapper.readValue(dataBuyRule1.getRule(), BuyRule.class);

        int x = 5;
    }*/




}