package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.AddDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.AndDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;

import java.util.ArrayList;
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
            if(res.value == 0.0) return res;
        }
        double dis = 0.0;
        for(Map.Entry<ProductStore,Integer> e : shoppingBag.entrySet()){
            if(e.getKey().getProductType().getCategory().value == category)
                dis +=  (discount/100) * (e.getKey().getPrice().value) * e.getValue();
        }
        return new DResponseObj<>(dis);
    }

    @Override
    public DResponseObj<String> getDiscountRule() {
        StringBuilder stringRule = new StringBuilder();
        if(id != 0)
            stringRule.append("And Discount Rule #").append(id).append(":\n\t");
        stringRule.append(rules.get(0).getDiscountRule().value).append(" AND\n\t");
        for (int i = 1; i < rules.size()-1; i++) {
            stringRule.append(rules.get(i).getDiscountRule().value).append(" AND\n\t");
        }
        stringRule.append(rules.get(rules.size()-1).getDiscountRule().value);
        if(id != 0) stringRule.append("\n\tSo all products from Category ").append(category).append(" have a ").append(discount).append("% discount");
        return new DResponseObj<>(stringRule.toString());
    }

    @Override
    public DResponseObj<DiscountRuleSL> convertToDiscountRuleSL() {
        List<DiscountRuleSL> rulesSL = new ArrayList<>();
        for(DiscountRule discountRule : rules){
            DResponseObj<DiscountRuleSL> discountRuleSL =  discountRule.convertToDiscountRuleSL();
            if(discountRuleSL.errorOccurred()) return discountRuleSL;
            rulesSL.add(discountRuleSL.value);
        }
        return new DResponseObj<>(new AndDiscountRuleSL(rulesSL,category,discount));
    }
}
