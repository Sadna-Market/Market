package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.concurrent.ConcurrentHashMap;

public abstract class LeafDiscountRule implements DiscountRule{

    protected double percentDiscount;
    protected int id;

    public LeafDiscountRule(double percentDiscount){
        this.percentDiscount = percentDiscount;
    }

    public abstract DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag);

    public abstract DResponseObj<String> getDiscountRule();

    public void setID(int id){
        this.id = id;
    }
}
