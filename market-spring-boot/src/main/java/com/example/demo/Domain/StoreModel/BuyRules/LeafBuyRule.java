package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Domain.UserModel.User;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.concurrent.ConcurrentHashMap;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property ="LeafBuyRule")
@JsonSubTypes({
        @JsonSubTypes.Type(value= UserBuyRule.class, name="UserBuyRule"),
        @JsonSubTypes.Type(value= ProductBuyRule.class, name="ProductBuyRule"),
        @JsonSubTypes.Type(value= CategoryBuyRule.class, name="CategoryBuyRule"),
        @JsonSubTypes.Type(value= ShoppingBagBuyRule.class, name="ShoppingBagBuyRule")
})
public abstract class LeafBuyRule implements BuyRule {

    protected Predicate pred;
    protected int id;

    @JsonCreator
    public LeafBuyRule(@JsonProperty("pred") Predicate pred) {
        this.pred = pred;
    }
/*

    public LeafBuyRule( Predicate pred) {
        this.pred = pred;
    }

    @JsonCreator
    public LeafBuyRule(){ }
*/

    @Override
    public abstract DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag);

/*    public abstract DResponseObj<String> getBuyRule();*/

    @Override
    public void setID(int id){
        this.id = id;
    }

    public abstract     DResponseObj<BuyRuleSL> convertToBuyRuleSL();

    public Predicate getPred() {
        return pred;
    }

    public int getId() {
        return id;
    }

}

