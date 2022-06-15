package com.example.demo.Service.ServiceObj.DiscountRules;


import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.StoreModel.DiscountRule.AndDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.XorDiscountRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.ArrayList;
import java.util.List;

public class XorDiscountRuleSL extends CompositionDiscountRuleSL {

    public String kind = "Xor Discount Rule";

    public String decision;

    public XorDiscountRuleSL(List<DiscountRuleSL> rules, String decision) {
        super(rules, 0.0);
        this.decision = decision;
    }


    //for convert
    public XorDiscountRuleSL(List<DiscountRuleSL> rules, String decision,int id) {
        super(rules, 0.0,id);
        this.decision = decision;
    }

    @Override
    public SLResponseOBJ<String> getDiscountRule() {
        StringBuilder stringRule = new StringBuilder();
        if (id != 0)
            stringRule.append("Xor Discount Rule #").append(id).append(":\n\t");
        stringRule.append(rules.get(0).getDiscountRule().value).append(" Xor\n\t");
        for (int i = 1; i < rules.size() - 1; i++) {
            stringRule.append(rules.get(i).getDiscountRule().value).append(" Xor\n\t");
        }
        stringRule.append(rules.get(rules.size()-1).getDiscountRule().value);
        stringRule.append("\n\tdecision rule is ");
        if (decision.equals("Big discount")) {
            stringRule.append(decision);
        } else {
            stringRule.append("first");
        }
        return new SLResponseOBJ<>(stringRule.toString());
    }

    @Override
    public SLResponseOBJ<DiscountRule> convertToDiscountRuleDL() {
        if(rules.size() < 2) return new SLResponseOBJ<>(ErrorCode.INVALID_ARGS_FOR_RULE);
        List<DiscountRule> rulesDL = new ArrayList<>();
        for(DiscountRuleSL discountRuleSL : rules){
            SLResponseOBJ<DiscountRule> discountRuleDL =  discountRuleSL.convertToDiscountRuleDL();
            if(discountRuleDL.errorOccurred()) return discountRuleDL;
            rulesDL.add(discountRuleDL.value);
        }
        return new SLResponseOBJ<>(new XorDiscountRule(rulesDL,decision));
    }
}
