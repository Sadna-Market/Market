package com.example.demo.Service.ServiceObj;

import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.ShoppingBag;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceShoppingBag {
    public ServiceStore store;
    public Map<Integer, Integer> productQuantity=new HashMap<>();

    public ServiceShoppingBag(ShoppingBag shoppingBag){
        productQuantity.putAll(shoppingBag.getProductQuantity().value);
        store= new ServiceStore(shoppingBag.getStore().value);
    }
    public Map<Integer,Integer> getProductQuantity(){
        return productQuantity;
    }
}
