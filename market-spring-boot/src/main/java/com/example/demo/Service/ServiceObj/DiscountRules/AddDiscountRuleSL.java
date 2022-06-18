package com.example.demo.Service.ServiceObj.DiscountRules;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.StoreModel.DiscountRule.AddDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.AndDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.ArrayList;
import java.util.List;

public class AddDiscountRuleSL extends CompositionDiscountRuleSL {

    public String kind = "Add Discount Rule";

    public AddDiscountRuleSL(List<DiscountRuleSL> rules){
        super(rules,0.0);
    }

    //for convert
    public AddDiscountRuleSL(List<DiscountRuleSL> rules,int id){
        super(rules,0.0,id);
    }

    @Override
    public SLResponseOBJ<String> getDiscountRule() {
        StringBuilder stringRule = new StringBuilder();
        if (id != 0)
            stringRule.append("Add Discount Rule #").append(id).append(":\n\t");
        stringRule.append(rules.get(0).getDiscountRule().value).append(" ADD\n\t");
        for (int i = 1; i < rules.size() - 1; i++) {
            stringRule.append(rules.get(i).getDiscountRule().value).append(" ADD\n\t");
        }
        stringRule.append(rules.get(rules.size()-1).getDiscountRule().value);
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
        return new SLResponseOBJ<>(new AddDiscountRule(rulesDL));
    }
}
