package com.example.demo.Domain.StoreModel.BuyRules;

import com.example.demo.Domain.Response.DResponseObj;
import com.example.demo.Domain.StoreModel.Predicate.Predicate;
import com.example.demo.Domain.StoreModel.Predicate.UserPred;
import com.example.demo.Domain.StoreModel.ProductStore;
import com.example.demo.Service.ServiceObj.BuyRules.BuyRuleSL;
import com.example.demo.Service.ServiceObj.BuyRules.UserBuyRuleSL;
import com.example.demo.Service.ServiceObj.DiscountRules.DiscountRuleSL;
import com.example.demo.Service.ServiceObj.Predicate.UserPredicateSL;

import java.util.concurrent.ConcurrentHashMap;

public class UserBuyRule extends LeafBuyRule{

    public UserBuyRule(UserPred pred) {
        super(pred);
    }

    @Override
    public DResponseObj<Boolean> passRule(String user,int age, ConcurrentHashMap<ProductStore, Integer> shoppingBag) {
        return pred.passRule(user,age,shoppingBag);
    }

    @Override
    public DResponseObj<String> getBuyRule() {
        String stringRule = "";
        if(id != 0)
            stringRule += "User Buy Rule #"+id + ":\n\t";
        stringRule += pred.getPredicateBuyRule();
        return new DResponseObj<>(stringRule);
    }

    @Override
    public DResponseObj<BuyRuleSL> convertToBuyRuleSL() {
        return new DResponseObj<>(new UserBuyRuleSL(new UserPredicateSL((UserPred) pred),id));
    }


}
