package com.example.demo.DataAccess.Services;

import com.example.demo.DataAccess.Entity.DataBuyRule;
import com.example.demo.DataAccess.Entity.DataDiscountRule;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.Store;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
        assertTrue(ruleService.insertDiscountRule(rule1));
    }

    @Test
    @Transactional
    void insertBuyPolicy() {
        String rule = "this is a rule";
        Store store1 = new Store("myStore1", new DiscountPolicy(), new BuyPolicy(), "niv@gmail.com");
        var dataStore = store1.getDataObject();
        assertTrue(storeService.insertStore(dataStore));

        /*
           Rule r = new Rule();
         * DataDiscountRule ddr = ruleBL.getDataObject();
         * ruleService.insertDiscountRule(ddr);
         * r.setId(ddr.getId); generated id
         * */
        DataBuyRule rule1 = new DataBuyRule();
        rule1.setRule(rule);
        rule1.setStore(dataStore);
        assertTrue(ruleService.insertBuyRule(rule1));
    }
}