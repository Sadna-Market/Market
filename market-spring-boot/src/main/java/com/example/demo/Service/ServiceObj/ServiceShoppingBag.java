package com.example.demo.Service.ServiceObj;

import com.example.demo.Domain.StoreModel.Store;
import com.example.demo.Domain.UserModel.ShoppingBag;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceShoppingBag {
    public ServiceStore store;
    public Map<Integer, Integer> productQuantityTESTS=new HashMap<>(); //only for test use
    public Map<Integer,ServiceProductStore> products = new HashMap<>();

    public ServiceShoppingBag(ShoppingBag shoppingBag){
        productQuantityTESTS.putAll(shoppingBag.getProductQuantity().value);
        shoppingBag.getProducts().value.forEach((id,product) -> {
            products.put(id,new ServiceProductStore(product.getQuantity().value,product.getPrice().value,product.getProductType().getProductID().value,product.getProductType().getProductName().value));
        });
        store = new ServiceStore(shoppingBag.getStore().value);
    }
    public ServiceShoppingBag() {
    }
        //only for tests use
        public Map<Integer,Integer> getProductQuantity(){
        return productQuantityTESTS;
    }
}
