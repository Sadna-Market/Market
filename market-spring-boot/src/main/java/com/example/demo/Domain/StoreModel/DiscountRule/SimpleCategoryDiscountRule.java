package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCategoryDiscountRule extends LeafDiscountRule{

    protected int categoryId;

    public SimpleCategoryDiscountRule(double percentDiscount, int categoryId){
        super(percentDiscount);
        this.categoryId = categoryId;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        double dis = 0.0;
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            if(e.getKey().getProductType().getCategory().value == categoryId)
                dis += (e.getKey().getPrice().value * e.getValue() * (percentDiscount/100));
        }
        return new DResponseObj<>(dis);
    }

}
