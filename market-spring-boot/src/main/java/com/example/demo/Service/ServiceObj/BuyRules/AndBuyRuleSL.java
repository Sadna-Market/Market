package com.example.demo.Service.ServiceObj.BuyRules;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.StoreModel.BuyRules.AndBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.ArrayList;
import java.util.List;

public class AndBuyRuleSL extends CompositionBuyRuleSL {

    public AndBuyRuleSL(List<BuyRuleSL> rules) {
        super(rules);
    }


    @Override
    public SLResponseOBJ<String> getBuyRule() {
        StringBuilder stringRule = new StringBuilder();
        if(id != 0)
            stringRule.append("And Buy Rule #").append(id).append(":\n\t");
        for(BuyRuleSL rule : rules)
            stringRule.append(rule.getBuyRule().value).append("\n\t");
        return new SLResponseOBJ<>(stringRule.toString());
    }

    @Override
    public SLResponseOBJ<BuyRule> convertToBuyRuleDL() {
        if(rules.size() < 2) return new SLResponseOBJ<>(ErrorCode.INVALID_ARGS_FOR_RULE);
        List<BuyRule> rulesDL = new ArrayList<>();
        for(BuyRuleSL buyRuleSL : rules){
            SLResponseOBJ<BuyRule> buyRuleDL =  buyRuleSL.convertToBuyRuleDL();
            if(buyRuleDL.errorOccurred()) return buyRuleDL;
            rulesDL.add(buyRuleDL.value);
        }
        return new SLResponseOBJ<>(new AndBuyRule(rulesDL));
    }


}
