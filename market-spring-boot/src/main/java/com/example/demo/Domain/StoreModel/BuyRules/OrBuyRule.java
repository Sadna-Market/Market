package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OrBuyRule extends CompositionBuyRule{


    public OrBuyRule(List<BuyRule> rules) {
        super(rules);
    }

    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        DResponseObj<Boolean> pass = new DResponseObj<>(true);
        for(BuyRule buyRule: rules){
            pass = buyRule.passRule(user,age,shoppingBag);
            if(pass.getValue()) return pass;
        }
        return new DResponseObj<>(false, pass.getErrorMsg()); //not passes all rules
    }
}
