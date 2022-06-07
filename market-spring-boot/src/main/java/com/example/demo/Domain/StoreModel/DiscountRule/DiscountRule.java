package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.concurrent.ConcurrentHashMap;

public interface DiscountRule {

    DResponseObj<Double> howMuchDiscount(String username, int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag);
    DResponseObj<String> getDiscountRule();
    void setID(int id);
    DResponseObj<DiscountRuleSL> convertToDiscountRuleSL();

}
