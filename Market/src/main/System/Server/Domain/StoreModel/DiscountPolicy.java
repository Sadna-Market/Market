package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Response.DResponseObj;

import java.util.concurrent.ConcurrentHashMap;

public class DiscountPolicy {
    public DResponseObj<Double> checkShoppingBag(String user, ConcurrentHashMap<Integer, Integer> productsInBag) {
        return new DResponseObj<>(0.0);
    }
}
