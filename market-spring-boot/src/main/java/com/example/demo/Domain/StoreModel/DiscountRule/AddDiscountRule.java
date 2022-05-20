package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AddDiscountRule extends CompositionDiscountRule{

    public AddDiscountRule(List<DiscountRule> rules, double discount){
        super(rules,discount);
    }
    @Override
    public DResponseObj<Double> howMuchDiscount(String username, int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        Double totalDiscount = 0.0;
        for(DiscountRule discountRule : rules){
            DResponseObj<Double> discount = discountRule.howMuchDiscount(username,age,shoppingBag);
            if(discount.errorOccurred()) continue; //not need to happen
            totalDiscount += discount.getValue();
        }
        return new DResponseObj<>(totalDiscount);

    }
}
