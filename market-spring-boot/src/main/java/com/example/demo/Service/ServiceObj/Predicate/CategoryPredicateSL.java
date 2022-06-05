package com.example.demo.Service.ServiceObj.Predicate;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

public class CategoryPredicateSL implements PredicateSL {

    public int category;
    public int minAge;
    public int minHour;  // for example 6 is like 6:00
    public int maxHour;  // for example 23 is like 23:00
    //for example no one can buy product from this category between 23:00 to 6:00

    public CategoryPredicateSL(int category){
        this.category = category;
        minAge = 0;
        minHour = 0;
        maxHour = 24;
    }

    public CategoryPredicateSL(int category, int minAge){
        this.category = category;
        this.minAge = minAge;
        this.minHour = 0;
        this.maxHour = 24;
    }

    public CategoryPredicateSL(int category, int minAge, int minHour, int maxHour){
        this.category = category;
        this.minAge = minAge;
        this.minHour = minHour;
        this.maxHour = maxHour;
    }

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

    public int getCategory() {
        return category;
    }

    @Override
    public SLResponseOBJ<Predicate> convertToPredicateDL() {
        if(category < 0 || minAge < 0 || !(minHour >= 0 && minHour <= 24) || !(maxHour >= 0 && maxHour <= 24) || maxHour < minHour)
            return new SLResponseOBJ<>(null, ErrorCode.INVALID_ARGS_FOR_RULE);
        return new SLResponseOBJ<>(new CategoryPred(category,minAge,minHour,maxHour));
    }

}
