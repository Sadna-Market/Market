package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// same like maximum
public class XorDiscountRule extends CompositionDiscountRule {

    protected String decision;

    public XorDiscountRule(List<DiscountRule> rules, double discount, String decision) {
        super(rules, discount);
        this.decision = decision;
    }

    @Override
    public DResponseObj<Double> howMuchDiscount(String username, int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        if (decision.equals("Big Discount")) {
            double maxDiscount = 0.0;
            for (DiscountRule discountRule : rules) {
                DResponseObj<Double> res = discountRule.howMuchDiscount(username, age, shoppingBag);
                if (res.errorOccurred()) continue;
                if (res.getValue() > maxDiscount) {
                    maxDiscount = res.getValue();
                }
            }
            return new DResponseObj<>(maxDiscount);
        } else {  // first discount
            for (DiscountRule discountRule : rules) {
                DResponseObj<Double> res = discountRule.howMuchDiscount(username, age, shoppingBag);
                if (res.errorOccurred()) continue;
                return new DResponseObj<>(res.getValue());
            }
            return new DResponseObj<>(0.0);
        }
    }
}
