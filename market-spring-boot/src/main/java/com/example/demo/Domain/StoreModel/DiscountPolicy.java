package com.example.demo.Domain.StoreModel;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Service.ServiceObj.ServiceDiscountPolicy;

import java.util.concurrent.ConcurrentHashMap;

public class DiscountPolicy {
    public DiscountPolicy(ServiceDiscountPolicy discountPolicy) {
    }

    public DiscountPolicy() {
    }

    public DResponseObj<Double> checkShoppingBag(String user, ConcurrentHashMap<Integer, Integer> productsInBag) {
        return new DResponseObj<>(0.0);
    }
}
