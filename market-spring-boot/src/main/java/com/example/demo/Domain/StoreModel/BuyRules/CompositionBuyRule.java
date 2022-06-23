package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property ="CompositionBuyRule")
@JsonSubTypes({
        @JsonSubTypes.Type(value=OrBuyRule.class, name="OrBuyRule"),
        @JsonSubTypes.Type(value=AndBuyRule.class, name="AndBuyRule"),
        @JsonSubTypes.Type(value=ConditioningBuyRule.class, name="ConditioningBuyRule")
})
public abstract class CompositionBuyRule implements BuyRule {

    protected List<BuyRule> rules;
    protected int id;

    @JsonCreator
    public CompositionBuyRule(@JsonProperty("rules") List<BuyRule> rules){
        if(rules != null)
            this.rules = Collections.synchronizedList(rules);
        else
            this.rules = Collections.synchronizedList(new LinkedList<>());
    }


    public void addRule(BuyRule buyRule){
        rules.add(buyRule);
    }

    public void deleteRule(BuyRule buyRule){
        rules.remove(buyRule);
    }

    public abstract DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag);

    @Override
    public void setID(int id) {
        this.id = id;
    }

    public abstract DResponseObj<BuyRuleSL> convertToBuyRuleSL();

    public List<BuyRule> getRules() {
        return rules;
    }

    public int getId() {
        return id;
    }

}
