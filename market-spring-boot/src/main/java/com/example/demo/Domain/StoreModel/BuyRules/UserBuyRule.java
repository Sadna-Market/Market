package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.DataAccess.Entity.DataBuyRule;
import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.UserBuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.Predicate.UserPredicateSL;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.ConcurrentHashMap;

@JsonTypeName("UserBuyRule")
public class UserBuyRule extends LeafBuyRule{

    @JsonCreator
    public UserBuyRule(@JsonProperty("pred") UserPred pred) {
        super(pred);
    }


/*    public UserBuyRule(UserPred pred) {
        super(pred);
    }

    @JsonCreator
    public UserBuyRule() {
        super();
    }*/


    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        return pred.passRule(user,age,shoppingBag);
    }

/*
    @Override
    public DResponseObj<String> getBuyRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "User Buy Rule #"+id + ":\n\t";
        stringRule += pred.getPredicateBuyRule();
        return new DResponseObj<>(stringRule);
    }
*/

    @Override
    public DResponseObj<BuyRuleSL> convertToBuyRuleSL() {
        return new DResponseObj<>(new UserBuyRuleSL(new UserPredicateSL((UserPred) pred),id));
    }



}
