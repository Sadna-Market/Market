package com.example.demo.Domain.StoreModel;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Service.ServiceObj.ServiceBuyPolicy;
import com.example.demo.Service.ServiceObj.ServiceDiscountPolicy;

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
}
