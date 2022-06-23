package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.AndBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.ConditioningBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.OrBuyRule;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property ="CompositionDiscountRule")
@JsonSubTypes({
        @JsonSubTypes.Type(value= OrDiscountRule.class, name="OrDiscountRule"),
        @JsonSubTypes.Type(value= AndDiscountRule.class, name="AndDiscountRule"),
        @JsonSubTypes.Type(value= XorDiscountRule.class, name="XorDiscountRule"),
        @JsonSubTypes.Type(value= AddDiscountRule.class, name="AddDiscountRule")
})
public abstract class CompositionDiscountRule implements DiscountRule{

    protected List<DiscountRule> rules;
    protected double discount;
    protected int id;

    @JsonCreator
    public CompositionDiscountRule(@JsonProperty("rules") List<DiscountRule> rules,@JsonProperty("dis")  double dis){
        if(rules != null)
            this.rules = Collections.synchronizedList(rules);
        else
            this.rules = Collections.synchronizedList(new LinkedList<>());
        discount = dis;
    }

    public abstract DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag);

    //public abstract DResponseObj<String> getDiscountRule();

    public void setID(int id){
        this.id = id;
    }

    public abstract DResponseObj<DiscountRuleSL> convertToDiscountRuleSL();

    public double getPercentDiscount() {
        return discount;
    }

    public List<DiscountRule> getRules() {
        return rules;
    }

    public double getDiscount() {
        return discount;
    }

    public int getId() {
        return id;
    }

}
