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
    void checkDiscountPolicyShoppingBagProductRule3(int i) {
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        products.replace(ps, i);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(pcRule).errorOccurred());
        DResponseObj<Double> res =discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products);
        assertEquals(0.0,res.getValue());
    }

    @DisplayName("checkDiscountPolicyShoppingBagProductRule- pass condition")
    @ParameterizedTest
    @ValueSource(ints = {3, 5, 6, 7})
    void checkDiscountPolicyShoppingBagProductRule4(int i) {
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        products.replace(ps, i);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(pcRule).errorOccurred());
        DResponseObj<Double> res =discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products);
        assertEquals(i*ps.getPrice().value*40/100,res.getValue());
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagRule2  -  successful")
    void checkDiscountPolicyStoreRule1() {
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(ssRule).errorOccurred());
        products.put(new ProductStore(new ProductTypeStab(2, "milk", "", 2), 25, 3.5), 6);
        assertEquals(19.25,discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products).value);
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagRule2  -  successful")
    void checkDiscountPolicyStoreRule2() {
        scRule = new ConditionStoreDiscountRule(new ShoppingBagPred(3, 2, 20),50);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(scRule).errorOccurred());
        products.put(new ProductStore(new ProductTypeStab(2, "milk", "", 2), 25, 3.5), 6);
        assertEquals(19.25,discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products).value);
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagRule3- not pass store condition - fail")
    void checkDiscountPolicyStoreRule3() {
        scRule = new ConditionStoreDiscountRule(new ShoppingBagPred(3, 2, 20),50);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(scRule).errorOccurred());
        assertEquals(0.0,discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products).value);
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagRule2  -  successful")
    void checkDiscountPolicyCategoryRule1() {
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(csRule).errorOccurred());
        assertEquals(1.75,discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products).value);
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagRule2  -  successful")
    void checkDiscountPolicyCategoryRule2() {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(ccRule).errorOccurred());
        products.put(new ProductStore(new ProductType(2, "milk", "", 2), 25, 3.5), 6);
        assertEquals(8.75,discountPolicy.checkDiscountPolicyShoppingBag("dor", 20, products).value);
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagRule3- not pass category condition - fail")
    void checkDiscountPolicyCategoryRule3() {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        assertEquals(0, discountPolicy.rulesSize());
        assertFalse(discountPolicy.addNewDiscountRule(ccRule).errorOccurred());
        products.put(new ProductStore(new ProductType(2, "milk", "", 2), 25, 3.5), 6);
        assertEquals(0.0,discountPolicy.checkDiscountPolicyShoppingBag("dor", 15, products).value);
    }



    @Test
    @DisplayName("checkDiscountPolicyShoppingBagAndRule2  -  successful")
    void checkDiscountPolicyShoppingBagAndRule2() {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(ccRule);
        discountRuleList.add(pcRule);
        AndDiscountRule and = new AndDiscountRule(discountRuleList,5,50);
        assertFalse(discountPolicy.addNewDiscountRule(and).errorOccurred());
        assertEquals(8.75,discountPolicy.checkDiscountPolicyShoppingBag("niv", 20, products).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,10,17})
    @DisplayName("checkDiscountPolicyShoppingBagAndRule3 - too young - fail")
    void checkDiscountPolicyShoppingBagAndRule3(int i) {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(ccRule);
        discountRuleList.add(pcRule);
        AndDiscountRule and = new AndDiscountRule(discountRuleList,5,50);
        assertFalse(discountPolicy.addNewDiscountRule(and).errorOccurred());
        assertEquals(0.0,discountPolicy.checkDiscountPolicyShoppingBag("niv", i, products).value);
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagOrRule1  -  successful")
    void checkDiscountPolicyShoppingBagOrRule1() {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(ccRule);
        discountRuleList.add(pcRule);
        OrDiscountRule or = new OrDiscountRule(discountRuleList,5,50);
        assertFalse(discountPolicy.addNewDiscountRule(or).errorOccurred());
        assertEquals(8.75,discountPolicy.checkDiscountPolicyShoppingBag("niv", 20, products).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,10,17})
    @DisplayName("checkDiscountPolicyShoppingBagOrRule3 - - successful")
    void checkDiscountPolicyShoppingBagOrRule3(int i) {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(ccRule);
        discountRuleList.add(pcRule);
        OrDiscountRule or = new OrDiscountRule(discountRuleList,5,50);
        assertFalse(discountPolicy.addNewDiscountRule(or).errorOccurred());
        assertEquals(8.75,discountPolicy.checkDiscountPolicyShoppingBag("niv", i, products).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,7,10,17})
    @DisplayName("checkDiscountPolicyShoppingBagOrRule4 - failure")
    void checkDiscountPolicyShoppingBagOrRule4(int i) {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        products.replace(ps,9);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(ccRule);
        discountRuleList.add(pcRule);
        OrDiscountRule or = new OrDiscountRule(discountRuleList,5,50);
        assertFalse(discountPolicy.addNewDiscountRule(or).errorOccurred());
        assertEquals(0.0,discountPolicy.checkDiscountPolicyShoppingBag("niv", i, products).value);
    }


    @Test
    @DisplayName("checkDiscountPolicyShoppingBagXOrRule1  - TT  successful")
    void checkDiscountPolicyShoppingBagXOrRule1() {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(ccRule);
        discountRuleList.add(pcRule);
        XorDiscountRule xor = new XorDiscountRule(discountRuleList,"Big Discount");
        assertFalse(discountPolicy.addNewDiscountRule(xor).errorOccurred());
        assertEquals(8.75,discountPolicy.checkDiscountPolicyShoppingBag("niv", 20, products).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,10,17})
    @DisplayName("checkDiscountPolicyShoppingBagXOrRule3 - TF successful")
    void checkDiscountPolicyShoppingBagXOrRule3(int i) {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(ccRule);
        discountRuleList.add(pcRule);
        XorDiscountRule xor = new XorDiscountRule(discountRuleList,"Big Discount");
        assertFalse(discountPolicy.addNewDiscountRule(xor).errorOccurred());
        assertEquals(3.5*5*(0.4),discountPolicy.checkDiscountPolicyShoppingBag("niv", i, products).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,9,17})
    @DisplayName("checkDiscountPolicyShoppingBagXOrRule4 - FT successful")
    void checkDiscountPolicyShoppingBagXOrRule4(int i) {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        products.replace(ps,i);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(ccRule);
        discountRuleList.add(pcRule);
        XorDiscountRule xor = new XorDiscountRule(discountRuleList,"Big Discount");
        assertFalse(discountPolicy.addNewDiscountRule(xor).errorOccurred());
        assertEquals(i*3.5*(0.5),discountPolicy.checkDiscountPolicyShoppingBag("niv", 20, products).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,9,17})
    @DisplayName("checkDiscountPolicyShoppingBagXOrRule5 - FF failure")
    void checkDiscountPolicyShoppingBagXOrRule5(int i) {
        ccRule = new ConditionCategoryDiscountRule(new CategoryPred(5,18),50);
        pcRule = new ConditionProductDiscountRule(new ProductPred(1, 3, 8),40);
        products.replace(ps,i);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(ccRule);
        discountRuleList.add(pcRule);
        XorDiscountRule xor = new XorDiscountRule(discountRuleList,"Big Discount");
        assertFalse(discountPolicy.addNewDiscountRule(xor).errorOccurred());
        assertEquals(0.0,discountPolicy.checkDiscountPolicyShoppingBag("niv", 15, products).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,9,15})
    @DisplayName("checkDiscountPolicyShoppingBagAddRule1 - successful")
    void checkDiscountPolicyShoppingBagAddRule1(int i) {
        products.replace(ps,i);
        List<DiscountRule> discountRuleList = new ArrayList<>();
        discountRuleList.add(csRule);
        discountRuleList.add(ssRule);
        AddDiscountRule add = new AddDiscountRule(discountRuleList);
        assertFalse(discountPolicy.addNewDiscountRule(add).errorOccurred());
        assertEquals(3.5*i*0.6,discountPolicy.checkDiscountPolicyShoppingBag("niv", 15, products).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,9,15})
    @DisplayName("combineDiscountRules - failure")
    void combineANDORDiscountRules(int i) {
        List<Integer> l = new ArrayList<>(); l.add(i); l.add(i+1);
        assertFalse(discountPolicy.combineANDORDiscountRules("and",l,i,4).value);
        assertFalse(discountPolicy.combineANDORDiscountRules("or",l,1,i).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,9,15})
    @DisplayName("combineDiscountRules - success")
    void combineANDORDiscountRulesSuccess(int i) {
        assertTrue(discountPolicy.addNewDiscountRule(ssRule).value);
        assertTrue(discountPolicy.addNewDiscountRule(psRule).value);
        List<Integer> l = new ArrayList<>(); l.add(1); l.add(2);
        assertTrue(discountPolicy.combineANDORDiscountRules("and",l,i,4).value);
        assertFalse(discountPolicy.combineANDORDiscountRules("or",l,1,i).value);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,9,15})
    @DisplayName("combineXorDiscountRules - failure")
    void combineXorDiscountRules(int i) {
        List<Integer> l = new ArrayList<>(); l.add(i); l.add(i+1);
        assertFalse(discountPolicy.combineXORDiscountRules(l,"Big discount").value);
        assertFalse(discountPolicy.combineXORDiscountRules(l,"asd").value);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,9,15})
    @DisplayName("combineXorDiscountRules - success")
    void combineXorDiscountRulesSuccess(int i) {
        assertTrue(discountPolicy.addNewDiscountRule(ssRule).value);
        assertTrue(discountPolicy.addNewDiscountRule(psRule).value);
        List<Integer> l = new ArrayList<>(); l.add(1); l.add(2);
        assertTrue(discountPolicy.combineXORDiscountRules(l,"Big discount").value);
        assertFalse(discountPolicy.combineXORDiscountRules(l,"Big discount").value);
    }
}

