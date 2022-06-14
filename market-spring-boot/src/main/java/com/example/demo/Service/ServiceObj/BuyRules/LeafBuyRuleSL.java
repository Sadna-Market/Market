package com.example.demo.Service.ServiceObj.BuyRules;

import com.example.demo.Domain.StoreModel.BuyRules.BuyRule;
import com.example.demo.Service.ServiceObj.Predicate.PredicateSL;
import com.example.demo.Service.ServiceResponse.SLResponseOBJ;


public abstract class LeafBuyRuleSL implements BuyRuleSL {
    public PredicateSL pred;
    public int id;


    public LeafBuyRuleSL(PredicateSL pred) {
        this.pred = pred;
    }

    public abstract SLResponseOBJ<String> getBuyRule();

    public abstract SLResponseOBJ<BuyRule> convertToBuyRuleDL();

}
