package com.example.demo.Domain.StoreModel;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.AndDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.OrDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.XorDiscountRule;
import com.example.demo.Service.ServiceObj.ServiceBuyPolicy;
import com.example.demo.Service.ServiceObj.ServiceDiscountPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscountPolicy {

    private ConcurrentHashMap<Integer, DiscountRule> rules;
    private AtomicInteger idCounter = new AtomicInteger(1);

    public DiscountPolicy(ServiceDiscountPolicy discountPolicy) {
        this.rules = new ConcurrentHashMap<>();
    }

    public DiscountPolicy() {
        this.rules = new ConcurrentHashMap<>();
    }


    public DResponseObj<Boolean> addNewDiscountRule(DiscountRule discountRule){
        if(discountRule.getPercentDiscount() < 0 || discountRule.getPercentDiscount() > 100) return new DResponseObj<>(false,ErrorCode.INVALID_PERECNT_DISCOUNT);
        int id = idCounter.getAndIncrement();
        rules.put(id,discountRule);
        discountRule.setID(id);
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> removeDiscountRule(int discountRuleID){
        DiscountRule removed = rules.remove(discountRuleID);
        return removed == null ? new DResponseObj<>(false, ErrorCode.DISCOUNT_RULE_NOT_EXIST) : new DResponseObj<>(true);
    }
    public DResponseObj<Double> checkDiscountPolicyShoppingBag(String username, int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        Double totalDiscount = 0.0;
        for(DiscountRule discountRule : rules.values()){
            DResponseObj<Double> discount = discountRule.howMuchDiscount(username,age,shoppingBag);
            if(discount.errorOccurred()) return discount;
            totalDiscount += discount.getValue();
        }
        return new DResponseObj<>(totalDiscount);
    }

    public int rulesSize(){
        return rules.size();
    }

    public ConcurrentHashMap<Integer,DiscountRule> getRules(){
        return rules;
    }

    public DResponseObj<Boolean> combineANDORDiscountRules(String operator, List<Integer> toCombineRules, int category, int discount) {
        List<DiscountRule> rulesForCombine = new ArrayList<>();
        for(Integer id : toCombineRules) {
            DiscountRule rule = rules.get(id);
            if (rule == null) return new DResponseObj<>(false, ErrorCode.INVALID_ARGS_FOR_RULE);
            rulesForCombine.add(rule);
        }
        DiscountRule combine;
        switch(operator) {
            case "and":
                combine = new AndDiscountRule(rulesForCombine, category,discount);
                break;
            case "or":
                combine = new OrDiscountRule(rulesForCombine, category, discount);
                break;
            default:
                return new DResponseObj<>(false, ErrorCode.INVALID_ARGS_FOR_RULE);
        }
        DResponseObj<Boolean> addCombine = addNewDiscountRule(combine);  // add the combine rule
        if(addCombine.errorOccurred()) return addCombine;
        toCombineRules.forEach(this::removeDiscountRule);  // remove all the rules that combine
        return new DResponseObj<>(true);
    }

    public DResponseObj<Boolean> combineXORDiscountRules(List<Integer> toCombineRules, String decision) {
        List<DiscountRule> rulesForCombine = new ArrayList<>();
        for(Integer id : toCombineRules) {
            DiscountRule rule = rules.get(id);
            if (rule == null) return new DResponseObj<>(false, ErrorCode.INVALID_ARGS_FOR_RULE);
            rulesForCombine.add(rule);
        }
        DiscountRule xor = new XorDiscountRule(rulesForCombine,decision);
        DResponseObj<Boolean> addCombine = addNewDiscountRule(xor);  // add the combine rule
        if(addCombine.errorOccurred()) return addCombine;
        toCombineRules.forEach(this::removeDiscountRule);  // remove all the rules that combine
        return new DResponseObj<>(true);
    }
}
