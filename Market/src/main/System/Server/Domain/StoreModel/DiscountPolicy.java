package main.System.Server.Domain.StoreModel;

import java.util.concurrent.ConcurrentHashMap;

public class DiscountPolicy {
    public double checkShoppingBag(String user, ConcurrentHashMap<Integer, Integer> productsInBag) {
        return 0.0;
    }
}
