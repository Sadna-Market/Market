package com.example.demo.Service.ServiceObj.DiscountRules;


import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.StoreModel.DiscountRule.AndDiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.DiscountRule;
import com.example.demo.Domain.StoreModel.DiscountRule.OrDiscountRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.ArrayList;
import java.util.List;

public class OrDiscountRuleSL extends CompositionDiscountRuleSL {

    public String kind = "Or Discount Rule";

    public int category;

    public OrDiscountRuleSL(List<DiscountRuleSL> rules, int category, double dis) {
        super(rules, dis);
        this.category = category;
    }

    //for convert
    public OrDiscountRuleSL(List<DiscountRuleSL> rules, int category, double dis,int id) {
        super(rules, dis,id);
        this.category = category;
    }

    @Override
    public SLResponseOBJ<String> getDiscountRule() {
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
        return new SLResponseOBJ<>(new OrDiscountRule(rulesDL,category,discount));
    }

}
