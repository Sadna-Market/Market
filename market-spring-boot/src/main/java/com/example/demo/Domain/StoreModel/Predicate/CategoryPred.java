package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryPred implements Predicate{
    private final int category;
    private final int minAgeToBuy;
    private final int minHourToBuy;  // for example 6 is like 6:00
    private final int maxHourToBuy;  // for example 23 is like 23:00
    //for example no one can buy product from this category between 23:00 to 6:00

    public CategoryPred(int category,int maxQuantityToBuy){
        this.category = category;
        minAgeToBuy = 0;
        minHourToBuy = 0;
        maxHourToBuy = 24;
    }

    public CategoryPred(int category,int maxQuantityToBuy, int minAgeToBuy){
        this.category = category;
        this.minAgeToBuy = minAgeToBuy;
        this.minHourToBuy = 0;
        this.maxHourToBuy = 24;
    }

    public CategoryPred(int category,int maxQuantityToBuy, int minAgeToBuy , int minHourToBuy , int maxHourToBuy){
        this.category = category;
        this.minAgeToBuy = minAgeToBuy;
        this.minHourToBuy = minHourToBuy;
        this.maxHourToBuy = maxHourToBuy;
    }

    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        int hourNow = LocalDateTime.now().getHour();
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            ProductStore product = e.getKey();
            if(product.getProductType().getCategory().getValue() == category) {
                if(age<minAgeToBuy) return new DResponseObj<>(false,ErrorCode.BUYER_TOO_YOUNG_TO_BUY_THIS_PRODUCT);
                if(hourNow < minHourToBuy || hourNow > maxHourToBuy)  return new DResponseObj<>(false,ErrorCode.BUYER_CAN_NOT_BUY_THIS_HOUR);
                return new DResponseObj<>(true);
            }
        }
        return new DResponseObj<>(true);
    }
}
