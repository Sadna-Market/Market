package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;

import java.util.concurrent.ConcurrentHashMap;

public abstract class LeafBuyRule implements BuyRule {
    protected Predicate pred;
    protected int id;

    public LeafBuyRule(Predicate pred) {
        this.pred = pred;
    }

    @Override
    public abstract DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag);

    public abstract DResponseObj<String> getBuyRule();

    @Override
    public void setID(int id){
        this.id = id;
    }

    public abstract     DResponseObj<BuyRuleSL> convertToBuyRuleSL();
    ;
}

