package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AndDiscountRule extends CompositionDiscountRule {

    protected int category;

    public AndDiscountRule(List<DiscountRule> rules,int category,double dis){
        super(rules,dis);
        this.category =category;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username, int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        for(DiscountRule discountRule : rules){
            DResponseObj<Double> res = discountRule.howMuchDiscount(username,age,shoppingBag);
            if(res.errorOccurred()) return res;
        }
        double dis = 0.0;
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            if(e.getKey().getProductType().getCategory().value == category)
                dis +=  (discount/100) * (e.getKey().getPrice().value) * e.getValue();
        }
        return new DResponseObj<>(dis);
    }
}
