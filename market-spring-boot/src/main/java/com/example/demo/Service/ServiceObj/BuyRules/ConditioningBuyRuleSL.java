package com.example.demo.Service.ServiceObj.BuyRules;
import com.example.demo.Domain.StoreModel.BuyRules.AndBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.ConditioningBuyRule;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;

import java.util.ArrayList;
import java.util.List;

public class ConditioningBuyRuleSL extends CompositionBuyRuleSL {

    public String kind = "Conditioning Buy Rule";

    public BuyRuleSL predIf;
    public BuyRuleSL predThen;


    public ConditioningBuyRuleSL(BuyRuleSL predIf , BuyRuleSL predThen) {
        super(null,-1);
        this.predIf = predIf;
        this.predThen = predThen;
    }

    //for convert
    public ConditioningBuyRuleSL(BuyRuleSL predIf , BuyRuleSL predThen, int id) {
        super(null,id);
        this.predIf = predIf;
        this.predThen = predThen;
    }

    @Override
    public SLResponseOBJ<String> getBuyRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Conditioning Buy Rule #"+id+":\n\t";
        stringRule += "if: " + predIf.getBuyRule().value +"\n\t";
        stringRule += "then: " + predThen.getBuyRule().value;
        return new SLResponseOBJ<>(stringRule);
    }

    @Override
    public SLResponseOBJ<BuyRule> convertToBuyRuleDL() {
        SLResponseOBJ<BuyRule> ifRuleDL = predIf.convertToBuyRuleDL();
        if(ifRuleDL.errorOccurred()) return ifRuleDL;
        SLResponseOBJ<BuyRule> thenRuleDL = predThen.convertToBuyRuleDL();
        if(thenRuleDL.errorOccurred()) return thenRuleDL;
        return new SLResponseOBJ<>(new ConditioningBuyRule(ifRuleDL.value,thenRuleDL.value));
    }


}
