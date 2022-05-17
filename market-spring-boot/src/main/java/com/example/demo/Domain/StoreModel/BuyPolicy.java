package com.example.demo.Domain.StoreModel;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Service.ServiceObj.ServiceBuyPolicy;

import java.util.concurrent.ConcurrentHashMap;

public class BuyPolicy {
    public BuyPolicy(ServiceBuyPolicy buyPolicy) {
    }

    public BuyPolicy() {
    }

    public DResponseObj<ConcurrentHashMap<Integer, Integer>> checkShoppingBag(String user, ConcurrentHashMap<Integer, Integer> shoppingBag) {
        return new DResponseObj<>(shoppingBag);
    }
}
