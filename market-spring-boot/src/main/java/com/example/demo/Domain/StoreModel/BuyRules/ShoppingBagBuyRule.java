package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.ShoppingBagPred;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.ShoppingBagBuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.ConditionStoreDiscountRuleSL;
import com.example.demo.Service.ServiceObj.Predicate.ShoppingBagPredicateSL;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.concurrent.ConcurrentHashMap;

@JsonTypeName("ShoppingBagBuyRule")
public class ShoppingBagBuyRule extends LeafBuyRule {

    @JsonCreator
    public ShoppingBagBuyRule(@JsonProperty("pred") ShoppingBagPred pred) {
        super(pred);
    }

    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        return pred.passRule(user,age,shoppingBag);
    }

/*
    @Override
    public DResponseObj<String> getBuyRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "ShoppingBag Buy Rule #"+id + ":\n\t";
        stringRule += pred.getPredicateBuyRule();
        return new DResponseObj<>(stringRule);
    }
*/

    @Override
    public DResponseObj<BuyRuleSL> convertToBuyRuleSL() {
        return new DResponseObj<>(new ShoppingBagBuyRuleSL(new ShoppingBagPredicateSL((ShoppingBagPred) pred),id));
    }

}
