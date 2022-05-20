package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleStoreDiscountRule extends LeafDiscountRule{

    public SimpleStoreDiscountRule(double percentDiscount){
        super(percentDiscount);
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        double dis = 0.0;
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            dis += (e.getKey().getPrice().value* e.getValue() * (percentDiscount/100));
        }
        return new DResponseObj<>(dis);
    }
}
