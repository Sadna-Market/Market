package main.System.Server.Domain.StoreModel;

import main.System.Server.Domain.Response.DResponseObj;
import main.System.Server.Domain.UserModel.ShoppingBag;

import java.util.concurrent.ConcurrentHashMap;

public class BuyPolicy {
    public DResponseObj<ConcurrentHashMap<Integer, Integer>> checkShoppingBag(String user, ConcurrentHashMap<Integer, Integer> shoppingBag) {
        return new DResponseObj<>(shoppingBag);
    }
}
