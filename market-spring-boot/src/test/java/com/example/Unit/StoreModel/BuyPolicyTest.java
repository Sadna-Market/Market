package com.example.Unit.StoreModel;

import Stabs.ProductTypeStab;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.BuyRules.*;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Domain.StoreModel.ProductStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class BuyPolicyTest {
    BuyPolicy buyPolicy;
    ProductBuyRule pRule;
    UserBuyRule uRule;
    ShoppingBagBuyRule sRule;
    CategoryBuyRule cRule;
    ConcurrentHashMap<ProductStore,Integer> products;
    ProductType product;
    ProductStore ps;

    @BeforeEach
    void setUp() {
        buyPolicy = new BuyPolicy();
        pRule = new ProductBuyRule(new ProductPred(1,3,8,true));
        uRule = new UserBuyRule(new UserPred("dor"));
        sRule = new ShoppingBagBuyRule(new ShoppingBagPred(10,2));
        cRule = new CategoryBuyRule(new CategoryPred(5,20));
        product = new ProductType(1,"gin","halcohol",5);
        ps = new ProductStore(product,41,3.5);
        products = new ConcurrentHashMap<>();
        products.put(ps,5);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    @DisplayName("addNewBuyRule  -  successful")
    void addNewBuyRule() {
        ProductBuyRule prule = new ProductBuyRule(new ProductPred(1,3,8,true));
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(prule,1).errorOccurred());
        assertEquals(1, buyPolicy.rulesSize());
    }

    @Test
    @DisplayName("removeNewBuyRule  -  successful")
    void removeBuyRule() {
        ProductBuyRule prule = new ProductBuyRule(new ProductPred(1,3,8,true));
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(prule,1).errorOccurred());
        assertEquals(1, buyPolicy.rulesSize());
        assertFalse(buyPolicy.removeBuyRule(1).errorOccurred());
        assertEquals(0, buyPolicy.rulesSize());
    }

    @Test
    @DisplayName("checkBuyPolicyWithoutRules  -  successful")
    void checkBuyPolicyShoppingBag() {
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkBuyPolicyShoppingBagProductRule2() {
        ProductBuyRule prule = new ProductBuyRule(new ProductPred(1,3,8,true));
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(prule,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- can buy flag - fail")
    void checkBuyPolicyShoppingBagProductRule3() {
        ProductBuyRule prule = new ProductBuyRule(new ProductPred(1,3,8,false));
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(prule,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }

    @DisplayName("checkBuyPolicyWithRules- not good quantity - fail")
    @ParameterizedTest
    @ValueSource(ints = {-1,2,9,100})
    void checkBuyPolicyShoppingBagProductRule4(int i) {
        ProductBuyRule p = new ProductBuyRule(new ProductPred(1,3,8,true));
        products.replace(ps,i);
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(p,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkBuyPolicyShoppingBagUserRule2() {
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(uRule,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("niv",20,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- user can't buy - fail")
    void checkBuyPolicyShoppingBagUserRule3() {
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(uRule,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkBuyPolicyShoppingBagRule2() {
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(sRule,1).errorOccurred());
        products.put(new ProductStore(new ProductTypeStab(2,"milk","",2),25,3.5),10);
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- not enough products - fail")
    void checkBuyPolicyShoppingBagRule3() {
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(sRule,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }

    @DisplayName("checkBuyPolicyWithRules- not good quantity - fail")
    @ParameterizedTest
    @ValueSource(ints = {0,2,4})
    void checkBuyPolicyShoppingBagRule4(int i) {
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(sRule,1).errorOccurred());
        products.put(new ProductStore(new ProductTypeStab(2,"milk","",2),25,3.5),i);
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }


    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkBuyPolicyShoppingBagCategoryRule2() {
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(cRule,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }


    @Test
    @DisplayName("checkBuyPolicyWithRules- too young - fail")
    void checkBuyPolicyShoppingBagCategoryRule3() {
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(cRule,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",16,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- can't buy this hour - fail")
    void checkBuyPolicyShoppingBagCategoryRule4() {
        assertEquals(0, buyPolicy.rulesSize());
        CategoryBuyRule ccRule = new CategoryBuyRule(new CategoryPred(5,20, 5,6));
        assertFalse(buyPolicy.addNewBuyRule(ccRule,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",20,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkBuyPolicyShoppingBagAndRule2() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(cRule);
        AndBuyRule and = new AndBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(and,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("niv",20,products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,2,4})
    @DisplayName("checkBuyPolicyWithRules- too young - fail")
    void checkBuyPolicyShoppingBagAndRule3() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(cRule);
        AndBuyRule and = new AndBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(and,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("niv",16,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- user cant buy- fail")
    void checkBuyPolicyShoppingBagAndRule4() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(cRule);
        AndBuyRule and = new AndBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(and,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",26,products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,2,4,17})
    @DisplayName("checkBuyPolicyWithRules- user cant buy and too young - fail")
    void checkBuyPolicyShoppingBagAndRule5(int i) {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(cRule);
        AndBuyRule and = new AndBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(and,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",i,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkBuyPolicyShoppingBagOrRule2() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(cRule);
        OrBuyRule or = new OrBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(or,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("niv",20,products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,2,4})
    @DisplayName("checkBuyPolicyWithRules- too young - successful")
    void checkBuyPolicyShoppingBagOrRule3() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(cRule);
        OrBuyRule or = new OrBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(or,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("niv",16,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- user cant buy- successful")
    void checkBuyPolicyShoppingBagOrRule4() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(cRule);
        OrBuyRule or = new OrBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(or,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor",26,products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,2,4,10,17})
    @DisplayName("checkBuyPolicyWithRules- user cant buy and too young - fail")
    void checkBuyPolicyShoppingBagOrRule5(int i) {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(cRule);
        OrBuyRule or = new OrBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(or,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor",i,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkBuyPolicyShoppingBagConditionRule2() {
        ConditioningBuyRule condition = new ConditioningBuyRule(uRule,cRule);
        assertFalse(buyPolicy.addNewBuyRule(condition,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("niv",20,products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,2,4,10,17})
    @DisplayName("checkBuyPolicyWithRules- too young - successful")
    void checkBuyPolicyShoppingBagConditionRule3(int i) {
        ConditioningBuyRule condition = new ConditioningBuyRule(uRule,cRule);
        assertFalse(buyPolicy.addNewBuyRule(condition,1).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("niv",i,products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- user cant buy- fail")
    void checkBuyPolicyShoppingBagConditionRule4() {
        ConditioningBuyRule condition = new ConditioningBuyRule(uRule,cRule);
        assertFalse(buyPolicy.addNewBuyRule(condition,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor",26,products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,2,4,10,17})
    @DisplayName("checkBuyPolicyWithRules- user cant buy and too young - fail")
    void checkBuyPolicyShoppingBagConditionRule5(int i) {
        ConditioningBuyRule condition = new ConditioningBuyRule(uRule,cRule);
        assertFalse(buyPolicy.addNewBuyRule(condition,1).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor",i,products).errorOccurred());
    }

}