package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.ErrorCode;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.AndBuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.OrBuyRuleSL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@JsonTypeName("OrBuyRule")
public class OrBuyRule extends CompositionBuyRule{

    @JsonCreator
    public OrBuyRule(@JsonProperty("rules") List<BuyRule> rules){
        super(rules);
    }


/*
    public OrBuyRule(List<BuyRule> rules){
        super(rules);
    }

    @JsonCreator
    public OrBuyRule(){
        super();
    }
*/

    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        DResponseObj<Boolean> pass = new DResponseObj<>(true);
        for(BuyRule buyRule: rules){
            pass = buyRule.passRule(user,age,shoppingBag);
            if(pass.getValue()) return pass;
        }
        return new DResponseObj<>(false, pass.getErrorMsg()); //not passes all rules
    }

/*
    @Override
    public DResponseObj<String> getBuyRule() {
        StringBuilder stringRule = new StringBuilder();
        if(id != 0)
            stringRule.append("Or Buy Rule #").append(id).append(":\n\t");
        for(BuyRule rule : rules)
            stringRule.append(rule.getBuyRule().value).append("\n\t");
        return new DResponseObj<>(stringRule.toString());
    }
*/

    @Override
    public DResponseObj<BuyRuleSL> convertToBuyRuleSL() {
        List<BuyRuleSL> rulesSL = new ArrayList<>();
        for(BuyRule buyRule : rules){
            DResponseObj<BuyRuleSL> buyRuleSL =  buyRule.convertToBuyRuleSL();
            if(buyRuleSL.errorOccurred()) return buyRuleSL;
            rulesSL.add(buyRuleSL.value);
        }
        return new DResponseObj<>(new OrBuyRuleSL(rulesSL,id));
    }
}
