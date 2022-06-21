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
    public BuyRule predIf;
    public BuyRule predThen;

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

        /*if(!condition.item1.passRule(user,shoppingBag)) return false;
         for (Tuple<Predicate,Predicate> condition : conditions){
            if(!condition.item1.passRule(user,shoppingBag)) continue;
            if(!condition.item1.passRule(user,shoppingBag)) return false;
        }
        return true; // pass all conditions*/
    }
/*

    @Override
    public DResponseObj<String> getBuyRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "Conditioning Buy Rule #"+id+":\n\t";
        stringRule += "if: " + predIf.getBuyRule().value +"\n\t";
        stringRule += "then: " + predThen.getBuyRule().value;
        return new DResponseObj<>(stringRule);
    }
*/

    @Override
    public DResponseObj<BuyRuleSL> convertToBuyRuleSL() {
        DResponseObj<BuyRuleSL> ifRule =  predIf.convertToBuyRuleSL();
        DResponseObj<BuyRuleSL> thenRule =  predThen.convertToBuyRuleSL();
        if(ifRule.errorOccurred()) return ifRule;
        if(thenRule.errorOccurred()) return thenRule;
        return new DResponseObj<>(new ConditioningBuyRuleSL(ifRule.value,thenRule.value,id));
    }

    /*class Tuple<E, T> {
        E item1;
        T item2;

        public Tuple(E item1, T item2) {
            this.item1 = item1;
            this.item2 = item2;
        }
    }*/

}

