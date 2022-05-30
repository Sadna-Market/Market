package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AddDiscountRule extends CompositionDiscountRule{

    public AddDiscountRule(List<DiscountRule> rules){
        super(rules,0.0);
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

    @Override
    public DResponseObj<String> getDiscountRule() {
        StringBuilder stringRule = new StringBuilder();
        if (id != 0)
            stringRule.append("Add Discount Rule #").append(id).append(":\n\t");
        stringRule.append(rules.get(0).getDiscountRule().value).append(" ADD\n\t");
        for (int i = 1; i < rules.size() - 1; i++) {
            stringRule.append(rules.get(i).getDiscountRule().value).append(" ADD\n\t");
        }
        stringRule.append(rules.get(rules.size()-1).getDiscountRule().value);
        return new DResponseObj<>(stringRule.toString());
    }

}
