package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.concurrent.ConcurrentHashMap;

public class ConditionCategoryDiscountRule extends SimpleCategoryDiscountRule{

    protected CategoryPred pred;

    public ConditionCategoryDiscountRule(CategoryPred pred,double percentDiscount, int categoryId) {
        super(percentDiscount, categoryId);
        this.pred = pred;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        if(!pred.passRule(username,age,shoppingBag).errorOccurred()){
            return super.howMuchDiscount(username,age,shoppingBag);
        }
        return new DResponseObj<>(0.0);
    }
}
