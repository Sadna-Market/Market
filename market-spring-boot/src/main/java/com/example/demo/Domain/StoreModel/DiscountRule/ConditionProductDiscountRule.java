package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.CategoryPred;
import com.example.demo.Domain.StoreModel.Predicate.ProductPred;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.concurrent.ConcurrentHashMap;

public class ConditionProductDiscountRule extends  SimpleProductDiscountRule{

    protected ProductPred pred;


    public ConditionProductDiscountRule(ProductPred pred,double percentDiscount, int productId) {
        super(percentDiscount, productId);
        this.pred = pred;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username, int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        if(!pred.passRule(username,age,shoppingBag).errorOccurred()){
            return super.howMuchDiscount(username,age,shoppingBag);
        }
        return new DResponseObj<>(0.0);
    }


}
