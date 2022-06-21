package com.example.demo.Domain.StoreModel.DiscountRule;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.BuyRules.CategoryBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.ProductBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.ShoppingBagBuyRule;
import com.example.demo.Domain.StoreModel.BuyRules.UserBuyRule;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.concurrent.ConcurrentHashMap;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property ="LeafDiscountRule")
@JsonSubTypes({
        @JsonSubTypes.Type(value= SimpleStoreDiscountRule.class, name="SimpleStoreDiscountRule"),
        @JsonSubTypes.Type(value= SimpleProductDiscountRule.class, name="SimpleProductDiscountRule"),
        @JsonSubTypes.Type(value= SimpleCategoryDiscountRule.class, name="SimpleCategoryDiscountRule")
})
public abstract class LeafDiscountRule implements DiscountRule{

    protected double percentDiscount;
    protected int id;

    @JsonCreator
    public LeafDiscountRule(@JsonProperty("percentDiscount") double percentDiscount){
        this.percentDiscount = percentDiscount;
    }

    public abstract DResponseObj<Double> howMuchDiscount(String username,int age,ConcurrentHashMap<ProductStore, Integer> shoppingBag);

    //public abstract DResponseObj<String> getDiscountRule();

    public void setID(int id){
        this.id = id;
    }

    public abstract DResponseObj<DiscountRuleSL> convertToDiscountRuleSL();

    public double getPercentDiscount() {
        return percentDiscount;
    }

    public int getId() {
        return id;
    }
}
