package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryPred implements Predicate{
    private final int category;
    private final int minAge;
    private final int minHour;  // for example 6 is like 6:00
    private final int maxHour;  // for example 23 is like 23:00
    //for example no one can buy product from this category between 23:00 to 6:00

    public CategoryPred(int category){
        this.category = category;
        minAge = 0;
        minHour = 0;
        maxHour = 24;
    }

    public CategoryPred(int category, int minAge){
        this.category = category;
        this.minAge = minAge;
        this.minHour = 0;
        this.maxHour = 24;
    }

    public CategoryPred(int category, int minAge, int minHour, int maxHour){
        this.category = category;
        this.minAge = minAge;
        this.minHour = minHour;
        this.maxHour = maxHour;
    }

    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        int hourNow = LocalDateTime.now().getHour();
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            ProductStore product = e.getKey();
            if(product.getProductType().getCategory().getValue() == category) {
                if(age< minAge) return new DResponseObj<>(false,ErrorCode.BUYER_TOO_YOUNG_TO_BUY_THIS_PRODUCT);
                if(hourNow < minHour || hourNow > maxHour)  return new DResponseObj<>(false,ErrorCode.BUYER_CAN_NOT_BUY_THIS_HOUR);
                return new DResponseObj<>(true);
            }
        }
        return new DResponseObj<>(true);
    }





}
