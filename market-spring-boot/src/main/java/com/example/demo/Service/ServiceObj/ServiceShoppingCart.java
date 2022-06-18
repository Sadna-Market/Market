package com.example.demo.Service.ServiceObj;


import com.example.demo.Domain.UserModel.ShoppingBag;
import com.example.demo.Domain.UserModel.ShoppingCart;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceShoppingCart {
    public Map<Integer, ServiceShoppingBag> shoppingBagHash = new HashMap<>();//<store id , shopping bag>

    public ServiceShoppingCart(ShoppingCart shoppingCart) {
        for (int storrId:shoppingCart.getHashShoppingCart().getValue().keySet()
             ) {
            ShoppingBag sb= shoppingCart.getHashShoppingCart().value.get(storrId);
            ServiceShoppingBag serviceShoppingBag = new ServiceShoppingBag(sb);
            shoppingBagHash.put(storrId,serviceShoppingBag);
        }
    }
    public ServiceShoppingCart() {
    }

        public List<List<Integer>> get() {
        List<List<Integer>> lst = new LinkedList<>();
        shoppingBagHash.forEach((i, bag) -> {
            List<Integer> l = new LinkedList<>(bag.getProductQuantity().values());
            lst.add(l);
        });
        return lst;
    }

}


