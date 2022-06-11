package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

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

    public abstract DResponseObj<DiscountRuleSL> convertToDiscountRuleSL();

    public double getPercentDiscount() {
        return percentDiscount;
    }
}
