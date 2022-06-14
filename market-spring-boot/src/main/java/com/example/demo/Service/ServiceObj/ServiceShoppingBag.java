package com.example.demo.Service.ServiceObj;

import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.ShoppingBag;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceShoppingBag {
    private Store store;
    private Map<Integer, Integer> productQuantity=new HashMap<>();

    public ServiceShoppingBag(ShoppingBag shoppingBag){
        productQuantity.putAll(shoppingBag.getProductQuantity().value);
        store=shoppingBag.getStore().value;
    }
    public Map<Integer,Integer> getProductQuantity(){
        return productQuantity;
    }
}
