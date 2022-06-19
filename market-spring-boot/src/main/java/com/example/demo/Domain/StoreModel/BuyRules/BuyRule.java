package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;

import java.util.concurrent.ConcurrentHashMap;

public interface BuyRule {
    DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag);
    DResponseObj<String> getBuyRule();
    void setID(int id);
    DResponseObj<BuyRuleSL> convertToBuyRuleSL();
}
