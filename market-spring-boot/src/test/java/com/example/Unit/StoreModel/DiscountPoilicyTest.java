package com.example.Unit.StoreModel;

import Stabs.ProductTypeStab;
import com.example.demo.Domain.Market.ProductType;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyPolicy;
import com.example.demo.Domain.StoreModel.BuyRules.*;
import com.example.demo.Domain.StoreModel.DiscountPolicy;
import com.example.demo.Domain.StoreModel.DiscountRule.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DiscountPoilicyTest {

    DiscountPolicy discountPolicy;
    SimpleProductDiscountRule psRule;
    SimpleStoreDiscountRule ssRule;
    SimpleCategoryDiscountRule csRule;
    ConditionProductDiscountRule pcRule;
    ConditionCategoryDiscountRule ccRule;
    ConditionStoreDiscountRule scRule;
    ConcurrentHashMap<ProductStore, Integer> products;
    ProductType product;
    ProductStore ps;

    @BeforeEach
    void setUp() {
        discountPolicy = new DiscountPolicy();
        psRule = new SimpleProductDiscountRule(50, 1);
        ssRule = new SimpleStoreDiscountRule(50);
        csRule = new SimpleCategoryDiscountRule(10, 5);
        product = new ProductType(1, "gin", "halcohol", 5);
        ps = new ProductStore(product, 41, 3.5);
        products = new ConcurrentHashMap<>();
        products.put(ps, 5);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    @DisplayName("addNewDiscountRule  -  successful")
    void addNewDiscountRule() {
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(psRule).errorOccurred());
        assertEquals(1, discountPolicy.rulesSize());
    }

    @Test
    @DisplayName("removeNewBuyRule  -  successful")
    void removeBuyRule() {
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(psRule).errorOccurred());
        assertEquals(1, discountPolicy.rulesSize());
        assertFalse(discountPolicy.removeDiscountRule(1).errorOccurred());
        assertEquals(0, discountPolicy.rulesSize());
    }

    @Test
    @DisplayName("checkDiscountPolicyShoppingBagWithoutRules  -  successful")
    void checkDiscountPolicyShoppingBag() {
        assertFalse(discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products).errorOccurred());
    }

    @Test
    @DisplayName("checkDiscountPolicyShoppingBagWithRules  -  successful")
    void checkBuyPolicyShoppingBagProductRule2() {
        assertFalse(discountPolicy.addNewDiscountRule(psRule).errorOccurred());
        DResponseObj<Double> res = discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products);
        assertFalse(res.errorOccurred());
        assertEquals(ps.getPrice().value*5*0.5,res.getValue());
    }




    @DisplayName("checkDiscountPolicyShoppingBagWithRules- not good quantity - fail")
    @ParameterizedTest
    @ValueSource(ints = {-1, 2, 9, 100})
    void checkDiscountPolicyShoppingBagProductRule4(int i) {
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40,1);
        products.replace(ps, i);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(pcRule).errorOccurred());
        DResponseObj<Double> res =discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products);
        assertEquals(0.0,res.getValue());
    }

    @DisplayName("checkDiscountPolicyShoppingBagProductRule- pass condition")
    @ParameterizedTest
    @ValueSource(ints = {3, 5, 6, 7})
    void checkDiscountPolicyShoppingBagProductRule5(int i) {
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40,1);
        products.replace(ps, i);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(pcRule).errorOccurred());
        DResponseObj<Double> res =discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products);
        assertEquals(i*ps.getPrice().value*40/100,res.getValue());
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagRule2  -  successful")
    void checkDiscountPolicyShoppingBagRule1() {
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(ssRule).errorOccurred());
        products.put(new ProductStore(new ProductTypeStab(2, "milk", "", 2), 25, 3.5), 6);
        assertEquals(19.25,discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products).value);
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagRule2  -  successful")
    void checkDiscountPolicyShoppingBagRule2() {
        scRule = new ConditionStoreDiscountRule(new ShoppingBagPred(3, 2, 20),50);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(scRule).errorOccurred());
        products.put(new ProductStore(new ProductTypeStab(2, "milk", "", 2), 25, 3.5), 6);
        assertEquals(19.25,discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products).value);
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagRule3- not pass store condition - fail")
    void checkDiscountPolicyShoppingBagRule3() {
        scRule = new ConditionStoreDiscountRule(new ShoppingBagPred(3, 2, 20),50);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(scRule).errorOccurred());
        assertEquals(0.0,discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products).value);
    }

   /* @DisplayName("checkBuyPolicyWithRules- not good quantity - fail")
    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4})
    void checkDiscountPolicyShoppingBagRule4(int i) {
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewBuyRule(ssRule).errorOccurred());
        products.put(new ProductStore(new ProductTypeStab(2, "milk", "", 2), 25, 3.5), i);
        assertTrue(discountPolicy.checkBuyPolicyShoppingBag("dor", 20, products).errorOccurred());
    }


    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkDiscountPolicyShoppingBagCategoryRule2() {
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(csRule).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor", 20, products).errorOccurred());
    }


    @Test
    @DisplayName("checkBuyPolicyWithRules- too young - fail")
    void checkDiscountPolicyShoppingBagCategoryRule3() {
        assertEquals(0, buyPolicy.rulesSize());
        assertFalse(buyPolicy.addNewBuyRule(csRule).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor", 16, products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- can't buy this hour - fail")
    void checkDiscountPolicyShoppingBagCategoryRule4() {
        assertEquals(0, buyPolicy.rulesSize());
        CategoryBuyRule ccRule = new CategoryBuyRule(new CategoryPred(5, 20, 5, 6));
        assertFalse(buyPolicy.addNewBuyRule(ccRule).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor", 20, products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkDiscountPolicyShoppingBagAndRule2() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(csRule);
        AndBuyRule and = new AndBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(and).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("niv", 20, products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4})
    @DisplayName("checkBuyPolicyWithRules- too young - fail")
    void checkDiscountPolicyShoppingBagAndRule3() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(csRule);
        AndBuyRule and = new AndBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(and).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("niv", 16, products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- user cant buy- fail")
    void checkDiscountPolicyShoppingBagAndRule4() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(csRule);
        AndBuyRule and = new AndBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(and).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor", 26, products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4, 17})
    @DisplayName("checkBuyPolicyWithRules- user cant buy and too young - fail")
    void checkDiscountPolicyShoppingBagAndRule5(int i) {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(csRule);
        AndBuyRule and = new AndBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(and).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor", i, products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkDiscountPolicyShoppingBagOrRule2() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(csRule);
        OrBuyRule or = new OrBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(or).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("niv", 20, products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4})
    @DisplayName("checkBuyPolicyWithRules- too young - successful")
    void checkDiscountPolicyShoppingBagOrRule3() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(csRule);
        OrBuyRule or = new OrBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(or).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("niv", 16, products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- user cant buy- successful")
    void checkDiscountPolicyShoppingBagOrRule4() {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(csRule);
        OrBuyRule or = new OrBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(or).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor", 26, products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4, 10, 17})
    @DisplayName("checkBuyPolicyWithRules- user cant buy and too young - fail")
    void checkDiscountPolicyShoppingBagOrRule5(int i) {
        List<BuyRule> buyRuleList = new ArrayList<>();
        buyRuleList.add(uRule);
        buyRuleList.add(csRule);
        OrBuyRule or = new OrBuyRule(buyRuleList);
        assertFalse(buyPolicy.addNewBuyRule(or).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("dor", i, products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules  -  successful")
    void checkDiscountPolicyShoppingBagConditionRule2() {
        ConditioningBuyRule condition = new ConditioningBuyRule(uRule, csRule);
        assertFalse(buyPolicy.addNewBuyRule(condition).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("niv", 20, products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4, 10, 17})
    @DisplayName("checkBuyPolicyWithRules- too young - successful")
    void checkDiscountPolicyShoppingBagConditionRule3(int i) {
        ConditioningBuyRule condition = new ConditioningBuyRule(uRule, csRule);
        assertFalse(buyPolicy.addNewBuyRule(condition).errorOccurred());
        assertTrue(buyPolicy.checkBuyPolicyShoppingBag("niv", i, products).errorOccurred());
    }

    @Test
    @DisplayName("checkBuyPolicyWithRules- user cant buy- fail")
    void checkDiscountPolicyShoppingBagConditionRule4() {
        ConditioningBuyRule condition = new ConditioningBuyRule(uRule, csRule);
        assertFalse(buyPolicy.addNewBuyRule(condition).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor", 26, products).errorOccurred());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4, 10, 17})
    @DisplayName("checkBuyPolicyWithRules- user cant buy and too young - fail")
    void checkDiscountPolicyShoppingBagConditionRule5(int i) {
        ConditioningBuyRule condition = new ConditioningBuyRule(uRule, csRule);
        assertFalse(buyPolicy.addNewBuyRule(condition).errorOccurred());
        assertFalse(buyPolicy.checkBuyPolicyShoppingBag("dor", i, products).errorOccurred());
    }*/

}

