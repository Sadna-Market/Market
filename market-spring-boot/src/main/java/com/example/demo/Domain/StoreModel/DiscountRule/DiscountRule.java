package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.concurrent.ConcurrentHashMap;

public interface DiscountRule {

    DResponseObj<Double> howMuchDiscount(String username, int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag);
    DResponseObj<String> getDiscountRule();
    void setID(int id);
}
