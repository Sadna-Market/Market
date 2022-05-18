package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AndBuyRule extends CompositionBuyRule {


    public AndBuyRule(List<BuyRule> rules) {
        super(rules);
    }


    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        for(BuyRule buyRule: rules){
            DResponseObj<Boolean> pass = buyRule.passRule(user,age,shoppingBag);
            if(!pass.getValue()) return pass;
        }
        return new DResponseObj<>(true); //passes all rules
    }
}
