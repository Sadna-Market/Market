package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CompositionDiscountRule implements DiscountRule{
    protected List<DiscountRule> rules;
    protected double discount;
    protected int id;

    public CompositionDiscountRule(List<DiscountRule> rules,double dis){
        if(rules != null)
            this.rules = Collections.synchronizedList(rules);
        else
            this.rules = Collections.synchronizedList(new LinkedList<>());
        discount = dis;
    }
    public abstract DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag);

    public abstract DResponseObj<String> getDiscountRule();

    public void setID(int id){
        this.id = id;
    }

    public abstract DResponseObj<DiscountRuleSL> convertToDiscountRuleSL();

    public double getPercentDiscount() {
        return discount;
    }

}
