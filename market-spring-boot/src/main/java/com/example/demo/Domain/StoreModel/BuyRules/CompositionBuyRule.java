package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CompositionBuyRule implements BuyRule {


    protected List<BuyRule> rules;

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
}
