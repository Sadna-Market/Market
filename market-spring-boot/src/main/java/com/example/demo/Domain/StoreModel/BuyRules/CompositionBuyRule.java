package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CompositionBuyRule implements BuyRule {


    protected List<BuyRule> rules;
    protected int id;

    public CompositionBuyRule(List<BuyRule> rules){
        if(rules != null)
            this.rules = Collections.synchronizedList(rules);
        else
            this.rules = Collections.synchronizedList(new LinkedList<>());
    }


    public void addRule(BuyRule buyRule){
        rules.add(buyRule);
    }

    public void deleteRule(BuyRule buyRule){
        rules.remove(buyRule);
    }

    public abstract DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag);

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public abstract DResponseObj<String> getBuyRule();

    public abstract     DResponseObj<BuyRuleSL> convertToBuyRuleSL();
    ;

}
