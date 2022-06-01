package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrDiscountRule extends CompositionDiscountRule {

    protected int category;

    public OrDiscountRule(List<DiscountRule> rules, int category, double dis) {
        super(rules, dis);
        this.category = category;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username, int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        boolean needToGetDiscount = false;
        for (DiscountRule discountRule : rules) {
            DResponseObj<Double> res = discountRule.howMuchDiscount(username, age, shoppingBag);
            if (!res.errorOccurred()) {
                needToGetDiscount = true;
                break;
            }
        }
        if(!needToGetDiscount) return new DResponseObj<>(0.0);
        double dis = 0.0;
        for (Map.Entry<ProductStore, Integer> e : shoppingBag.entrySet()) {
            if (e.getKey().getProductType().getCategory().value == category)
                dis += (discount / 100) * (e.getKey().getPrice().value) * e.getValue();
        }
        return new DResponseObj<>(dis);
    }

    @Override
    public DResponseObj<String> getDiscountRule() {
        StringBuilder stringRule = new StringBuilder();
        if (id != 0)
            stringRule.append("Or Discount Rule #").append(id).append(":\n\t");
        stringRule.append(rules.get(0).getDiscountRule().value).append(" OR\n\t");
        for (int i = 1; i < rules.size() - 1; i++) {
            stringRule.append(rules.get(i).getDiscountRule().value).append(" OR\n\t");
        }
        stringRule.append(rules.get(rules.size()-1).getDiscountRule().value);
        if (id != 0)
            stringRule.append("\n\tSo all products from Category ").append(category).append(" have a ").append(discount).append("% discount");
        return new DResponseObj<>(stringRule.toString());
    }

}
