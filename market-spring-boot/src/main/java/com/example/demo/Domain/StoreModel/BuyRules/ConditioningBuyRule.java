package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.AndBuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.ConditioningBuyRuleSL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@JsonTypeName("ConditioningBuyRule")
public class ConditioningBuyRule extends CompositionBuyRule {

    protected BuyRule predIf;
    protected BuyRule predThen;

/*
    public ConditioningBuyRule(List<BuyRule> rules) {
        super(rules);
    }
*/
    @JsonCreator
    public ConditioningBuyRule(@JsonProperty("predIf") BuyRule predIf ,@JsonProperty("predThen") BuyRule predThen) {
        super(null);
        //check before that lists size equal;
        this.predIf = predIf;
        this.predThen = predThen;
    }


    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        DResponseObj<Boolean> ifPass = predIf.passRule(user,age,shoppingBag);
        DResponseObj<Boolean> thenPass = predThen.passRule(user,age,shoppingBag);
        if(ifPass.getValue() && !thenPass.getValue())
            return new DResponseObj<>(false,thenPass.errorMsg);
        return new DResponseObj<>(true);

    }

    @Override
    public DResponseObj<BuyRuleSL> convertToBuyRuleSL() {
        DResponseObj<BuyRuleSL> ifRule =  predIf.convertToBuyRuleSL();
        DResponseObj<BuyRuleSL> thenRule =  predThen.convertToBuyRuleSL();
        if(ifRule.errorOccurred()) return ifRule;
        if(thenRule.errorOccurred()) return thenRule;
        return new DResponseObj<>(new ConditioningBuyRuleSL(ifRule.value,thenRule.value,id));
    }


    public BuyRule getPredIf() {
        return predIf;
    }

    public BuyRule getPredThen() {
        return predThen;
    }

}

