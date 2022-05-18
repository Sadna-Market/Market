package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingBagPred implements Predicate{
    int minProductQuantity;
    int minProductTypes;

    public ShoppingBagPred(int minProductQuantity, int minProductTypes){
        this.minProductQuantity = minProductQuantity;
        this.minProductTypes = minProductTypes;
    }
    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        int productQuantity = 0;
        int numOfTypes = 0;
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()) {
            productQuantity++;
            numOfTypes += e.getValue();
        }
        return productQuantity >= minProductQuantity && numOfTypes >= minProductTypes ?
                new DResponseObj<>(true) : new DResponseObj<>(false, ErrorCode.NOT_PASS_THE_MIN_QUANTITY_TO_BUY);
    }
}
