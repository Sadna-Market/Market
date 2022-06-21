package com.example.demo.Domain.StoreModel.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Qualifier("CategoryPred")
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

    @JsonCreator
    public CategoryPred(@JsonProperty("category") int category,@JsonProperty("minAge") int minAge,@JsonProperty("minHour") int minHour,@JsonProperty("maxHour") int maxHour){
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
/*

    @Override
    public String getPredicateBuyRule() {
        String s = "categoryID " + category +" : the minimum age to buy is "+ minAge;
        if(minHour != 0 || maxHour !=24) s+= " and no one can buy products from this category between " + maxHour+":00 to "+ minHour +":00";
        return s;
    }

    @Override
    public String getPredicateDiscountRule() {
        String s = "If you buy from category "+category+" and your age is at least "+minAge;
        if(minHour != 0 || maxHour !=24) s+=  " and time is between " + minHour+":00 to "+ maxHour +":00";
        return s;
    }
*/

    public int getCategory() {
        return category;
    }
    public int getMinAge() {
        return minAge;
    }

    public int getMinHour() {
        return minHour;
    }

    public int getMaxHour() {
        return maxHour;
    }

}
