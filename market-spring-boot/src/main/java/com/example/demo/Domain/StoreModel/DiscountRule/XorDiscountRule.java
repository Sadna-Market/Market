package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.AddDiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.XorDiscountRuleSL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// same like maximum
@JsonTypeName("XorDiscountRule")
public class XorDiscountRule extends CompositionDiscountRule {

    protected String decision;

    @JsonCreator
    public XorDiscountRule(@JsonProperty("rules") List<DiscountRule> rules,@JsonProperty("decision") String decision) {
        super(rules, 0.0);
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
                if (res.value == 0.0) continue;
                return new DResponseObj<>(res.getValue());
            }
            return new DResponseObj<>(0.0);
        }
    }


/*
    @Override
    public DResponseObj<String> getDiscountRule() {
        StringBuilder stringRule = new StringBuilder();
        if (id != 0)
            stringRule.append("Xor Discount Rule #").append(id).append(":\n\t");
        stringRule.append(rules.get(0).getDiscountRule().value).append(" Xor\n\t");
        for (int i = 1; i < rules.size() - 1; i++) {
            stringRule.append(rules.get(i).getDiscountRule().value).append(" Xor\n\t");
        }
        stringRule.append(rules.get(rules.size()-1).getDiscountRule().value);
        stringRule.append("\n\tdecision rule is ");
        if (decision.equals("Big Discount")) {
            stringRule.append(decision);
        } else {
            stringRule.append("first");
        }
        return new DResponseObj<>(stringRule.toString());
    }
*/

    @Override
    public DResponseObj<DiscountRuleSL> convertToDiscountRuleSL() {
        List<DiscountRuleSL> rulesSL = new ArrayList<>();
        for(DiscountRule discountRule : rules){
            DResponseObj<DiscountRuleSL> discountRuleSL =  discountRule.convertToDiscountRuleSL();
            if(discountRuleSL.errorOccurred()) return discountRuleSL;
            rulesSL.add(discountRuleSL.value);
        }
        return new DResponseObj<>(new XorDiscountRuleSL(rulesSL,decision,id));
    }

    public String getDecision() {
        return decision;
    }
}
